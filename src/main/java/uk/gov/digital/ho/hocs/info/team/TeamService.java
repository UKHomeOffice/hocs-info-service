package uk.gov.digital.ho.hocs.info.team;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.digital.ho.hocs.info.dto.PermissionDto;
import uk.gov.digital.ho.hocs.info.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;
import uk.gov.digital.ho.hocs.info.entities.Permission;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.entities.Unit;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.CaseTypeRepository;
import uk.gov.digital.ho.hocs.info.repositories.TeamRepository;
import uk.gov.digital.ho.hocs.info.repositories.UnitRepository;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
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

    public TeamService(TeamRepository teamRepository, UnitRepository unitRepository, CaseTypeRepository caseTypeRepository, KeycloakService keycloakService) {
        this.teamRepository = teamRepository;
        this.keycloakService = keycloakService;
        this.unitRepository = unitRepository;
        this.caseTypeRepository = caseTypeRepository;
    }

    public Set<TeamDto> getTeamsForUnit(UUID unitUUID) {
        return teamRepository.findTeamsByUnitUuid(unitUUID).stream().map(team -> TeamDto.from(team)).collect(Collectors.toSet());
    }

    public TeamDto getTeam(UUID teamUUID) {
        Team team = teamRepository.findByUuid(teamUUID);
        if(team == null) {
            throw new EntityNotFoundException("Team does not exist");
        }
        return TeamDto.from(team);
    }

    @Transactional
    public TeamDto createTeam(TeamDto newTeam, UUID unitUUID) {

        Team team = teamRepository.findByUuid(newTeam.getUuid());
        Unit unit = unitRepository.findByUuid(unitUUID);
        Set<Permission> permissions = getPermissionsFromDto(newTeam.getPermissions(), team);
        if(team == null) {
            team = new Team(newTeam.getDisplayName(),newTeam.getUuid(), true);
            team.addPermissions(getPermissionsFromDto(newTeam.getPermissions(), team));
            unit.addTeam(team);
         }
        createKeyCloakMappings(permissions,team.getUuid(),unit.getShortCode(), Optional.empty());

        log.info("Team with UUID {} created in Unit {}", team.getUuid().toString(), unit.getShortCode(), value(EVENT, TEAM_CREATED));
        return TeamDto.from(team);
    }

    @Transactional
    public void updateTeamName(UUID teamUUID, String newName) {
        Team team = teamRepository.findByUuid(teamUUID);
        team.setDisplayName(newName);
        log.info("Team with UUID {} name updated to {}", team.getUuid().toString(), newName, value(EVENT, TEAM_RENAMED));
    }

    public void addUserToTeam(UUID userUUID, UUID teamUUID) {

        Team team = teamRepository.findByUuid(teamUUID);
        String unit = team.getUnit().getShortCode();
        Set<Permission> permissions = team.getPermissions();

        createKeyCloakMappings(permissions,teamUUID,unit, Optional.of(userUUID));

        log.info("Added user with UUID {} to team with UUID {}",userUUID.toString(), team.getUuid().toString(), value(EVENT, USER_ADDED_TO_TEAM));
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

        log.info("Moved team {} from Unit {} to Unit {}", teamUUID.toString(), oldUnit.getShortCode(), newUnit.getShortCode(), value(EVENT, TEAM_ADDED_TO_UNIT));
    }

    public void updateTeamPermissions(UUID teamUUID, Set<PermissionDto> permissionsDto) {
        Team team = teamRepository.findByUuid(teamUUID);
        Set<Permission> permissions = getPermissionsFromDto(permissionsDto, team);
        team.addPermissions(permissions);
        createKeyCloakMappings(permissions,teamUUID,team.getUnit().getShortCode(), Optional.empty());
        keycloakService.updateUserGroupsForGroup(String.format("/%s/%s",team.getUnit().getShortCode(), teamUUID.toString()));
        log.info("Updated Permissions for team {}", teamUUID.toString(), value(EVENT, TEAM_PERMISSIONS_UPDATED));
    }

    @Transactional
    public void deleteTeam(UUID teamUUID) {
        //TODO: Check Team does not have any topics assigned before deletion
        Team team = teamRepository.findByUuid(teamUUID);
        team.setActive(false);
    }

    private Set<Permission> getPermissionsFromDto(Set<PermissionDto> permissionsDto, Team team) {
        Set<Permission> permissions = new HashSet<>();
        for(PermissionDto permissionDto: permissionsDto) {
            AccessLevel accessLevel = permissionDto.getAccessLevel();
            CaseTypeEntity caseType = caseTypeRepository.findByType(permissionDto.getCaseTypeCode());
            permissions.add(new Permission(accessLevel, team, caseType));
        }
        return permissions;
    }


    private void createKeyCloakMappings(Set<Permission> permissions, UUID teamUUID, String unitShortCode, Optional<UUID> userUUID) {
        String team = teamUUID.toString();
        keycloakService.createUnitGroupIfNotExists(unitShortCode);
        keycloakService.createGroupPathIfNotExists(unitShortCode, team);

        for(Permission permission: permissions) {
            keycloakService.createGroupPathIfNotExists(String.format("/%s/%s",unitShortCode, team), permission.getCaseType().getType());
            keycloakService.createGroupPathIfNotExists(String.format("/%s/%s/%s",unitShortCode,team, permission.getCaseType().getType()),permission.getAccessLevel().toString());
            String groupPath = String.format("/%s/%s/%s/%s", unitShortCode, team, permission.getCaseType().getType(), permission.getAccessLevel());

            if(userUUID.isPresent()) {
                keycloakService.addUserToGroup(userUUID.get(), groupPath);
            }

        }
    }


}
