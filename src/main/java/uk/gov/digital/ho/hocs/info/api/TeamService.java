package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.digital.ho.hocs.info.api.dto.CreateTeamDto;
import uk.gov.digital.ho.hocs.info.api.dto.PermissionDto;
import uk.gov.digital.ho.hocs.info.api.dto.TeamDeleteActiveParentTopicsDto;
import uk.gov.digital.ho.hocs.info.client.auditClient.AuditClient;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.dto.GetTopicResponse;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.*;
import uk.gov.digital.ho.hocs.info.domain.repository.ParentTopicRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.UnitRepository;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.util.*;

import static net.logstash.logback.argument.StructuredArguments.value;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.*;

@Service
@Slf4j
public class TeamService {

    private TeamRepository teamRepository;
    private KeycloakService keycloakService;
    private UnitRepository unitRepository;
    private CaseTypeService caseTypeService;
    private ParentTopicRepository parentTopicRepository;
    private AuditClient auditClient;
    private CaseworkClient caseworkClient;

    public TeamService(TeamRepository teamRepository, UnitRepository unitRepository, CaseTypeService caseTypeService,ParentTopicRepository parentTopicRepository, KeycloakService keycloakService, AuditClient auditClient, CaseworkClient caseworkClient) {
        this.teamRepository = teamRepository;
        this.keycloakService = keycloakService;
        this.unitRepository = unitRepository;
        this.caseTypeService = caseTypeService;
        this.parentTopicRepository = parentTopicRepository;
        this.auditClient = auditClient;
        this.caseworkClient = caseworkClient;
    }

    public Set<Team> getTeamsForUnit(UUID unitUUID) {
        log.debug("Getting all Teams for Unit {}", unitUUID);
        Set<Team> teams = teamRepository.findTeamsByUnitUuid(unitUUID);
        log.info("Got {} Teams", teams.size());
        return teams;
    }

    @Cacheable("teams")
    public Set<Team> getAllActiveTeams() {
        return getTeams();
    }

    @CachePut("teams")
    public Set<Team> refreshTeamCache() {
        return getTeams();
    }

    private Set<Team> getTeams() {
        log.debug("Getting all active Teams");
        Set<Team> activeTeams = teamRepository.findAllByActiveTrue();
        log.info("Got {} active Teams", activeTeams.size());
        return activeTeams;
    }

    public Set<Team> getAllActiveTeamsByUnitShortCode(String unitShortCode) {
        log.debug("Getting all active Teams by Unit ShortCode {}", unitShortCode );
        Set<Team> activeTeams = teamRepository.findAllByActiveTrueAndUnitShortCodeEquals(unitShortCode);
        log.info("Got {} active Teams by Unit ShortCode {}", activeTeams.size(), unitShortCode);
        return activeTeams;
    }

    private Team getTeamByDisplayName(String displayName) {
        log.debug("Getting team with displayName {}", displayName);
        Team team = teamRepository.findByDisplayName(displayName);
        if (team != null) {
            log.info("Got team With displayName {}", displayName);
            return team;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Team not found for displayName {}", displayName);
        }
    }

    public Team getTeam(UUID teamUUID) {
        log.debug("Getting Team {}", teamUUID);
        Team team = teamRepository.findByUuid(teamUUID);
        if (team != null) {
            log.info("Got Team {}", teamUUID);
            return team;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Team not found for UUID %s", teamUUID);
        }
    }

    public Set<Team> getTeamsForUser(UUID userUUID) {
        log.debug("Getting teams for user {}", userUUID);
        Set<UUID> teamUUIDs = keycloakService.getGroupsForUser(userUUID);
        Set<Team> teams = new HashSet<>();
        teamUUIDs.forEach((teamUUID) -> {
            try {
                teams.add(getTeam(teamUUID));
            } catch (ApplicationExceptions.EntityNotFoundException e){
                log.error("Team not found for UUID {}", teamUUID);
            }
        });
        return teams;
    }

    public Team getTeamByTopicAndStage(UUID caseUUID, UUID topicUUID, String stageType) {
        log.debug("Getting Team for Topic {} and Stage {}", topicUUID, stageType);
        GetTopicResponse topicResponse = caseworkClient.getTopic(caseUUID, topicUUID);
        Team team = teamRepository.findByTopicAndStage(topicResponse.getTopicUUID(), stageType);
        if (team != null) {
            log.info("Got Team for Topic {} and Stage {}", topicUUID, stageType);
            return team;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Team not found for Topic %s and Stage %s", topicUUID, stageType);
        }
    }

    @Transactional
    public Team createTeam(CreateTeamDto newTeam, UUID unitUUID) {
        String newTeamDisplayName = newTeam.getDisplayName();
        log.debug("Creating Team {}", newTeamDisplayName);
        Team existingTeam = teamRepository.findByDisplayName(newTeamDisplayName);
        Unit unit = unitRepository.findByUuid(unitUUID);

        if (existingTeam == null) {
            log.debug("Team {} doesn't exist, creating.", newTeamDisplayName);
            Set<String> caseTypeUUIDs = newTeam.getCaseTypes();
            Set<CaseType> caseTypes = new HashSet<>();
            Set<PermissionDto> permissionsDto = new HashSet<>();

            Team teamToAdd = new Team(newTeamDisplayName, true);
            teamToAdd.setUnit(unit);
            teamRepository.save(teamToAdd);

            Team team = teamRepository.findByDisplayName(newTeamDisplayName);
            updateTeamPermissions(team.getUuid(), permissionsDto);

            caseTypeUUIDs.forEach(caseType -> caseTypes.add(caseTypeService.getCaseType(caseType)));
            caseTypes.forEach(caseType -> {
                log.debug("Adding CaseType {} to new Team {}", caseType, newTeamDisplayName);
                permissionsDto.add(new PermissionDto(caseType.getType(), AccessLevel.from(5)));
            });

            unit.addTeam(team);
            unitRepository.save(unit);

            createKeyCloakMappings(team.getUuid(), Optional.empty());
            auditClient.createTeamAudit(team);
            auditClient.updateTeamPermissionsAudit(team.getUuid(), permissionsDto);

            log.info("Team with UUID {} created in Unit {}", team.getUuid().toString(), unit.getShortCode(), value(EVENT, TEAM_CREATED));

            return team;
        } else {
            log.debug("Team {} exists, not creating.", newTeamDisplayName);
            throw new ApplicationExceptions.EntityAlreadyExistsException("Team %s already exists", newTeamDisplayName);
        }
    }

    @Transactional
    public void updateTeamName(UUID teamUUID, String newName) {
        log.debug("Updating Team {} name", teamUUID);
        Team team = getTeam(teamUUID);
        team.setDisplayName(newName);
        auditClient.renameTeamAudit(team);
        log.info("Team with UUID {} name updated to {}", team.getUuid().toString(), newName, value(EVENT, TEAM_RENAMED));
    }

    @Transactional
    public void updateTeamLetterName(UUID teamUUID, String newLetterName) {
        log.debug("Updating Team {} letter name", teamUUID);
        Team team = getTeam(teamUUID);
        team.setLetterName(newLetterName);
        auditClient.renameTeamAudit(team);
        log.info("Team with UUID {} letter name updated to {}", team.getUuid().toString(), newLetterName, value(EVENT, TEAM_RENAMED));
    }

    @CacheEvict(value = "teamMembers", key = "#teamUUID")
    public void addUserToTeam(UUID userUUID, UUID teamUUID) {
        log.debug("Adding User {} to Team {}", userUUID, teamUUID);
        Team team = getTeam(teamUUID);
        createKeyCloakMappings(teamUUID, Optional.of(userUUID));
        auditClient.addUserToTeamAudit(userUUID, team);
        log.info("Added user with UUID {} to team with UUID {}", userUUID.toString(), team.getUuid().toString(), value(EVENT, USER_ADDED_TO_TEAM));
    }

    @Transactional
    public void moveToNewUnit(UUID unitUUID, UUID teamUUID) {
        log.debug("Moving Team {} to Unit {}", teamUUID, unitUUID);
        Team team = getTeam(teamUUID);
        Unit oldUnit = unitRepository.findByUuid(team.getUnit().getUuid());
        oldUnit.removeTeam(teamUUID);

        Unit newUnit = unitRepository.findByUuid(unitUUID);
        newUnit.addTeam(team);

        auditClient.moveToNewUnitAudit(teamUUID.toString(), oldUnit.getShortCode(), newUnit.getShortCode());
        log.info("Moved team {} from Unit {} to Unit {}", teamUUID.toString(), oldUnit.getShortCode(), newUnit.getShortCode(), value(EVENT, TEAM_ADDED_TO_UNIT));
    }

    public void updateTeamPermissions(UUID teamUUID, Set<PermissionDto> permissionsDto) {
        log.debug("Updating Team {} with {} permissions", permissionsDto.size());
        Team team = getTeam(teamUUID);
        Set<Permission> permissions = getPermissionsFromDto(permissionsDto, team);
        team.addPermissions(permissions);
        createKeyCloakMappings(teamUUID, Optional.empty());
        auditClient.updateTeamPermissionsAudit(teamUUID, permissionsDto);
        log.info("Updated Permissions for team {}", teamUUID.toString(), value(EVENT, TEAM_PERMISSIONS_UPDATED));
    }

    @Transactional
    public void deleteTeamPermissions(UUID teamUUID, Set<PermissionDto> permissionsDto) {
        log.debug("Deleting {} Team permissions for Team {}", permissionsDto.size(), teamUUID);
        Team team = getTeam(teamUUID);
        Set<Permission> permissions = getPermissionsFromDto(permissionsDto, team);
        team.deletePermissions(permissions);
        auditClient.deleteTeamPermissionsAudit(teamUUID, permissionsDto);
        log.info("Deleted Permission for team {}", teamUUID.toString(), value(EVENT, TEAM_PERMISSIONS_DELETED));
    }

    @Transactional
    public void deleteTeam(UUID teamUUID) {
        log.debug("Deleting Team {}", teamUUID);
        List<ParentTopic> parentTopics = parentTopicRepository.findAllActiveParentTopicsForTeam(teamUUID);
        if(parentTopics.isEmpty()) {
            log.debug("No topics assigned to Team {}, safe to delete", teamUUID);
            Team team = getTeam(teamUUID);
            team.setActive(false);
            auditClient.deleteTeamAudit(team);
            log.info("Deleted team {}", teamUUID, value(EVENT, TEAM_DELETED));
        } else {
            String msg = "Unable to delete team as active parent topic are assigned to team";
            log.error(msg, value(EVENT, TEAM_DELETED_FAILURE));
            throw new ApplicationExceptions.TeamDeleteException(msg, TeamDeleteActiveParentTopicsDto.from(parentTopics, msg));
        }
    }

    private Set<Permission> getPermissionsFromDto(Set<PermissionDto> permissionsDto, Team team) {
        Set<Permission> permissions = new HashSet<>(permissionsDto.size());
        for (PermissionDto permissionDto : permissionsDto) {
            AccessLevel accessLevel = permissionDto.getAccessLevel();
            CaseType caseType = caseTypeService.getCaseType(permissionDto.getCaseTypeCode());
            permissions.add(new Permission(accessLevel, team, caseType));
        }
        return permissions;
    }

    private void createKeyCloakMappings(UUID teamUUID, Optional<UUID> userUUID) {
        keycloakService.createTeamGroupIfNotExists(teamUUID);
        userUUID.ifPresent(uuid -> keycloakService.addUserToTeam(uuid, teamUUID));
    }

    @Transactional
    @CacheEvict(value = "teamMembers", allEntries = true)
    public void removeUserFromTeam(UUID userUUID, UUID teamUUID) {
        log.debug("Removing User {} from Team {}", userUUID, teamUUID);

        if (caseworkClient.getCasesForUser(userUUID, teamUUID).isEmpty()) {
            keycloakService.removeUserFromTeam(userUUID, teamUUID);
            auditClient.removeUserFromTeamAudit(userUUID, teamUUID);
            log.info("Removed user with UUID {} from team with UUID {}", userUUID.toString(), teamUUID.toString(), value(EVENT, USER_REMOVED_FROM_TEAM));
        } else {
            throw new ApplicationExceptions.UserRemoveException("Unable to remove user {} from team {} as user has assigned cases", userUUID, teamUUID);
        }
    }

     Set<Team> findActiveTeamsByUnitUuid(UUID unitUUID) {
         log.debug("Getting active teams for Unit {}", unitUUID);
         return teamRepository.findActiveTeamsByUnitUuid(unitUUID);
    }
}
