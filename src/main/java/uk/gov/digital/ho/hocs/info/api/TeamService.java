package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.digital.ho.hocs.info.api.dto.PermissionDto;
import uk.gov.digital.ho.hocs.info.api.dto.TeamDeleteActiveParentTopicsDto;
import uk.gov.digital.ho.hocs.info.api.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.client.auditClient.AuditClient;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.dto.GetTopicResponse;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.*;
import uk.gov.digital.ho.hocs.info.domain.repository.ParentTopicRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.UnitRepository;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;
import uk.gov.digital.ho.hocs.info.security.Base64UUID;
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

    public Set<Team> getAllActiveTeams() {
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
    public Team createTeam(TeamDto newTeam, UUID unitUUID) {
        log.debug("Creating Team {}", newTeam.getDisplayName());
        Team team = teamRepository.findByUuid(newTeam.getUuid());
        Unit unit = unitRepository.findByUuid(unitUUID);
        Set<Permission> permissions = getPermissionsFromDto(newTeam.getPermissions(), team);
        if (team == null) {
            log.debug("Team {} doesn't exist, creating.", newTeam.getDisplayName());
            team = new Team(newTeam.getDisplayName(), newTeam.getUuid(), true);
            team.addPermissions(getPermissionsFromDto(newTeam.getPermissions(), team));
            unit.addTeam(team);
        } else {
            log.debug("Team {} exists, not creating.", newTeam.getDisplayName());
        }
        createKeyCloakMappings(permissions, team.getUuid(), Optional.empty());
        auditClient.createTeamAudit(team);
        log.info("Team with UUID {} created in Unit {}", team.getUuid().toString(), unit.getShortCode(), value(EVENT, TEAM_CREATED));
        return team;
    }

    @Transactional
    public void updateTeamName(UUID teamUUID, String newName) {
        log.debug("Updating Team {} name", teamUUID);
        Team team = getTeam(teamUUID);
        team.setDisplayName(newName);
        auditClient.renameTeamAudit(team);
        log.info("Team with UUID {} name updated to {}", team.getUuid().toString(), newName, value(EVENT, TEAM_RENAMED));
    }

    @CacheEvict(value = "teamMembers", key = "#teamUUID")
    public void addUserToTeam(UUID userUUID, UUID teamUUID) {
        log.debug("Adding User {} to Team {}", userUUID, teamUUID);
        Team team = getTeam(teamUUID);
        Set<Permission> permissions = team.getPermissions();

        createKeyCloakMappings(permissions, teamUUID, Optional.of(userUUID));

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
        Set<String> permissionPaths = createKeyCloakMappings(permissions, teamUUID, Optional.empty());;
        keycloakService.updateUserTeamGroups(team.getUuid(), permissionPaths);

        auditClient.updateTeamPermissionsAudit(teamUUID, permissionsDto);
        log.info("Updated Permissions for team {}", teamUUID.toString(), value(EVENT, TEAM_PERMISSIONS_UPDATED));
    }

    @Transactional
    public void deleteTeamPermissions(UUID teamUUID, Set<PermissionDto> permissionsDto) {
        log.debug("Deleting {} Team permissions for Team {}", permissionsDto.size(), teamUUID);
        Team team = getTeam(teamUUID);
        String encodedTeamUUID = Base64UUID.UUIDToBase64String(teamUUID);
        Set<Permission> permissions = getPermissionsFromDto(permissionsDto, team);
        team.deletePermissions(permissions);
        Set<String> permissionPathsAccessLevel = new HashSet<>(permissions.size());
        Set<String> permissionPathsCaseTypeLevel = new HashSet<>(permissions.size());
        permissions.forEach(permission -> {
            permissionPathsAccessLevel.add(String.format("/%s/%s/%s", encodedTeamUUID, permission.getCaseType().getType(),permission.getAccessLevel().getLevel()));
            permissionPathsCaseTypeLevel.add(String.format("/%s/%s", encodedTeamUUID, permission.getCaseType().getType()));
        });

        permissionPathsAccessLevel.forEach(permissionPath -> keycloakService.deleteTeamPermisisons(permissionPath));

        if (team.getPermissions().isEmpty()) {
            permissionPathsCaseTypeLevel.forEach(permissionPath -> keycloakService.deleteTeamPermisisons(permissionPath));
        }

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

    private Set<String> createKeyCloakMappings(Set<Permission> permissions, UUID teamUUID, Optional<UUID> userUUID) {
        String team = Base64UUID.UUIDToBase64String(teamUUID);
        keycloakService.createTeamGroupIfNotExists(team);

        Set<String> permissionPaths = new HashSet<>(permissions.size());
        for (Permission permission : permissions) {
            keycloakService.createGroupPathIfNotExists(String.format("/%s", team), permission.getCaseType().getType());
            keycloakService.createGroupPathIfNotExists(String.format("/%s/%s", team,permission.getCaseType().getType()), String.valueOf(permission.getAccessLevel().getLevel()));
            String permissionPath = String.format("/%s/%s/%s", team, permission.getCaseType().getType(), String.valueOf(permission.getAccessLevel().getLevel()));
            permissionPaths.add(permissionPath);
            userUUID.ifPresent(uuid -> keycloakService.addUserToGroup(uuid, permissionPath));
        }
        return permissionPaths;
    }
}
