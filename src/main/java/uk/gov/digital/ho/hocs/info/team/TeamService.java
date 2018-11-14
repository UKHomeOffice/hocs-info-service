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
    private UnitRepository unitRepostiory;
    private CaseTypeRepository caseTypeRepository;

    public TeamService(TeamRepository teamRepository, UnitRepository unitRepository, CaseTypeRepository caseTypeRepository, KeycloakService keycloakService) {
        this.teamRepository = teamRepository;
        this.keycloakService = keycloakService;
        this.unitRepostiory = unitRepository;
        this.caseTypeRepository = caseTypeRepository;
    }

    public Set<TeamDto> getTeamsForUnit(UUID unitUUID) {
        return teamRepository.findTeamsByUnitUUID(unitUUID).stream().map(team -> TeamDto.from(team)).collect(Collectors.toSet());
    }

    public TeamDto getTeam(UUID teamUUID) {
        return TeamDto.from( teamRepository.findByUuid(teamUUID));
    }

    @Transactional
    public void createTeam(TeamDto newTeam, UUID unitUUID) {

        Team team = teamRepository.findByUuid(newTeam.getUuid());
        Unit unit = unitRepostiory.findByUuid(unitUUID);
        Set<Permission> permissions = getPermissionsFromDto(newTeam.getPermissions(), team);
        if(team == null) {
            team = new Team(newTeam.getDisplayName(),newTeam.getUuid());
            team.addPermissions(getPermissionsFromDto(newTeam.getPermissions(), team));
            unit.addTeam(team);
           // unitRepostiory.save(unit);
         }
        CreateKeyCloakMappings(permissions,team.getUuid(),unit.getShortCode(), Optional.empty());

        log.info("Team with UUID {} created in Unit {}", team.getUuid().toString(), unit.getShortCode(), value(EVENT, TEAM_CREATED));
    }



    public void addUserToTeam(UUID userUUID, UUID teamUUID) {

        Team team = teamRepository.findByUuid(teamUUID);
        String unit = team.getUnit().getShortCode();
        String teamId= team.getUuid().toString();
        Set<Permission> permissions = team.getPermissions();

        CreateKeyCloakMappings(permissions,teamUUID,unit, Optional.of(userUUID));

        log.info("Added user with UUID {} to team with UUID {}",userUUID.toString(), team.getUuid().toString(), value(EVENT, USER_ADDED_TO_TEAM));
    }

    @Transactional
    public void moveToNewUnit(UUID unitUUID, UUID teamUUID) {
        Team team = teamRepository.findByUuid(teamUUID);
        String currentGroupPath = String.format("/%s/%s", team.getUnit().getShortCode(), team.getUuid());

        Unit oldUnit = unitRepostiory.findByUuid(team.getUnitUUID());
        oldUnit.removeTeam(teamUUID);

        Unit newUnit = unitRepostiory.findByUuid(unitUUID);
        newUnit.addTeam(team);

        keycloakService.moveGroup(currentGroupPath, team.getUnit().getShortCode());

        log.info("Moved team {} from Unit {} to Unit {}", teamUUID.toString(), oldUnit.getShortCode(), newUnit.getShortCode(), value(EVENT, TEAM_ADDED_TO_UNIT));
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

    private void CreateKeyCloakMappings(Set<Permission> permissions, UUID teamUUID, String unitShortCode, Optional<UUID> userUUID) {
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
