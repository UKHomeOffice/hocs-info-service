package uk.gov.digital.ho.hocs.info.team;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.digital.ho.hocs.info.auditClient.AuditClient;
import uk.gov.digital.ho.hocs.info.dto.PermissionDto;
import uk.gov.digital.ho.hocs.info.dto.TeamDeleteActiveParentTopicsDto;
import uk.gov.digital.ho.hocs.info.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.entities.*;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.exception.TeamDeleteException;
import uk.gov.digital.ho.hocs.info.repositories.CaseTypeRepository;
import uk.gov.digital.ho.hocs.info.repositories.ParentTopicRepository;
import uk.gov.digital.ho.hocs.info.repositories.TeamRepository;
import uk.gov.digital.ho.hocs.info.repositories.UnitRepository;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.util.*;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.value;
import static uk.gov.digital.ho.hocs.info.logging.LogEvent.*;

@Service
@Slf4j
public class TeamService {

    private TeamRepository teamRepository;
    private KeycloakService keycloakService;
    private UnitRepository unitRepository;
    private CaseTypeRepository caseTypeRepository;
    private ParentTopicRepository parentTopicRepository;
    private AuditClient auditClient;

    public TeamService(TeamRepository teamRepository, UnitRepository unitRepository, CaseTypeRepository caseTypeRepository,ParentTopicRepository parentTopicRepository, KeycloakService keycloakService, AuditClient auditClient) {
        this.teamRepository = teamRepository;
        this.keycloakService = keycloakService;
        this.unitRepository = unitRepository;
        this.caseTypeRepository = caseTypeRepository;
        this.parentTopicRepository = parentTopicRepository;
        this.auditClient = auditClient;
    }

    public Set<TeamDto> getTeamsForUnit(UUID unitUUID) {
        return teamRepository.findTeamsByUnitUuid(unitUUID).stream().map(team -> TeamDto.from(team)).collect(Collectors.toSet());
    }


    public Set<TeamDto> getAllActiveTeams() {
        return teamRepository.findAllByActiveTrue().stream().map(team -> TeamDto.fromWithoutPermissions(team)).collect(Collectors.toSet());

    }

    public TeamDto getTeam(UUID teamUUID) {
        Team team = teamRepository.findByUuid(teamUUID);
        if (team == null) {
            throw new EntityNotFoundException("Team does not exist");
        }
        return TeamDto.from(team);
    }

    @Transactional
    public TeamDto createTeam(TeamDto newTeam, UUID unitUUID) {

        Team team = teamRepository.findByUuid(newTeam.getUuid());
        Unit unit = unitRepository.findByUuid(unitUUID);
        Set<Permission> permissions = getPermissionsFromDto(newTeam.getPermissions(), team);
        if (team == null) {
            team = new Team(newTeam.getDisplayName(), newTeam.getUuid(), true);
            team.addPermissions(getPermissionsFromDto(newTeam.getPermissions(), team));
            unit.addTeam(team);
        }
        createKeyCloakMappings(permissions, team.getUuid(), unit.getShortCode(), Optional.empty());

        auditClient.createTeamAudit(team);
        log.info("Team with UUID {} created in Unit {}", team.getUuid().toString(), unit.getShortCode(), value(EVENT, TEAM_CREATED));
        return TeamDto.from(team);
    }

    @Transactional
    public void updateTeamName(UUID teamUUID, String newName) {
        Team team = teamRepository.findByUuid(teamUUID);
        team.setDisplayName(newName);
        auditClient.renameTeamAudit(team);
        log.info("Team with UUID {} name updated to {}", team.getUuid().toString(), newName, value(EVENT, TEAM_RENAMED));
    }

    public void addUserToTeam(UUID userUUID, UUID teamUUID) {

        Team team = teamRepository.findByUuid(teamUUID);
        String unit = team.getUnit().getShortCode();
        Set<Permission> permissions = team.getPermissions();

        createKeyCloakMappings(permissions, teamUUID, unit, Optional.of(userUUID));

        auditClient.addUserToTeamAudit(userUUID, team);
        log.info("Added user with UUID {} to team with UUID {}", userUUID.toString(), team.getUuid().toString(), value(EVENT, USER_ADDED_TO_TEAM));
    }

    @Transactional
    public void moveToNewUnit(UUID unitUUID, UUID teamUUID) {
        Team team = teamRepository.findByUuid(teamUUID);
        String currentGroupPath = String.format("/%s/%s", team.getUnit().getShortCode(), team.getUuid());

        Unit oldUnit = unitRepository.findByUuid(team.getUnit().getUuid());
        oldUnit.removeTeam(teamUUID);

        Unit newUnit = unitRepository.findByUuid(unitUUID);
        newUnit.addTeam(team);

        keycloakService.moveGroup(currentGroupPath, team.getUnit().getShortCode());

        auditClient.moveToNewUnitAudit(teamUUID.toString(), oldUnit.getShortCode(), newUnit.getShortCode());
        log.info("Moved team {} from Unit {} to Unit {}", teamUUID.toString(), oldUnit.getShortCode(), newUnit.getShortCode(), value(EVENT, TEAM_ADDED_TO_UNIT));
    }

    public void updateTeamPermissions(UUID teamUUID, Set<PermissionDto> permissionsDto) {
        Team team = teamRepository.findByUuid(teamUUID);
        Set<Permission> permissions = getPermissionsFromDto(permissionsDto, team);
        team.addPermissions(permissions);
        Set<String> permissionPaths = createKeyCloakMappings(permissions, teamUUID, team.getUnit().getShortCode(), Optional.empty());
        String teamPath = String.format("/%s/%s", team.getUnit().getShortCode(), teamUUID.toString());
        createKeyCloakMappings(permissions, teamUUID, team.getUnit().getShortCode(), Optional.empty());
        keycloakService.updateUserTeamGroups(teamPath, permissionPaths);
        auditClient.updateTeamPermissionsAudit(teamUUID, permissionsDto);
        log.info("Updated Permissions for team {}", teamUUID.toString(), value(EVENT, TEAM_PERMISSIONS_UPDATED));
    }

    @Transactional
    public void deleteTeamPermissions(UUID teamUUID, Set<PermissionDto> permissionsDto) {
        Team team = teamRepository.findByUuid(teamUUID);
        Set<Permission> permissions = getPermissionsFromDto(permissionsDto, team);
        team.deletePermissions(permissions);
        Set<String> permissionPathsAccessLevel = new HashSet<>();
        Set<String> permissionPathsCaseTypeLevel = new HashSet<>();
        permissions.forEach(permission -> {
            permissionPathsAccessLevel.add(String.format("/%s/%s/%s/%s", team.getUnit().getShortCode(), teamUUID.toString(), permission.getCaseType().getType(), permission.getAccessLevel().toString()));
            permissionPathsCaseTypeLevel.add(String.format("/%s/%s/%s", team.getUnit().getShortCode(), teamUUID.toString(), permission.getCaseType().getType()));
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
        List<ParentTopic> parentTopics = parentTopicRepository.findAllActiveParentTopicsForTeam(teamUUID);
        if(parentTopics.isEmpty()) {
            log.info("parent topics is empty");
            Team team = teamRepository.findByUuid(teamUUID);
            team.setActive(false);
            auditClient.deleteTeamAudit(team);
            log.info("Deleted team {}", teamUUID.toString(), value(EVENT, TEAM_DELETED));
        } else {

            String msg = "Unable to delete team as active parent topic are assigned to team";
            log.error(msg, value(EVENT, TEAM_DELETED_FAILURE));
            throw new TeamDeleteException(msg, TeamDeleteActiveParentTopicsDto.from(parentTopics, msg));
        }
    }

    private Set<Permission> getPermissionsFromDto(Set<PermissionDto> permissionsDto, Team team) {
        Set<Permission> permissions = new HashSet<>();
        for (PermissionDto permissionDto : permissionsDto) {
            AccessLevel accessLevel = permissionDto.getAccessLevel();
            CaseTypeEntity caseType = caseTypeRepository.findByType(permissionDto.getCaseTypeCode());
            permissions.add(new Permission(accessLevel, team, caseType));
        }
        return permissions;
    }


    private Set<String> createKeyCloakMappings(Set<Permission> permissions, UUID teamUUID, String unitShortCode, Optional<UUID> userUUID) {
        String team = teamUUID.toString();
        keycloakService.createUnitGroupIfNotExists(unitShortCode);
        keycloakService.createGroupPathIfNotExists(unitShortCode, team);

        if (userUUID.isPresent()) {
            keycloakService.addUserToGroup(userUUID.get(), String.format("/%s/%s", unitShortCode, team));
        }

        Set<String> permissionPaths = new HashSet<>();
        for (Permission permission : permissions) {
            keycloakService.createGroupPathIfNotExists(String.format("/%s/%s", unitShortCode, team), permission.getCaseType().getType());
            keycloakService.createGroupPathIfNotExists(String.format("/%s/%s/%s", unitShortCode, team, permission.getCaseType().getType()), permission.getAccessLevel().toString());
            String permissionPath = String.format("/%s/%s/%s/%s", unitShortCode, team, permission.getCaseType().getType(), permission.getAccessLevel());
            permissionPaths.add(permissionPath);
            if (userUUID.isPresent()) {
                keycloakService.addUserToGroup(userUUID.get(), permissionPath);
            }

        }
        return permissionPaths;
    }
}
