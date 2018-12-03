package uk.gov.digital.ho.hocs.info.security;


import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.PartialImportRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.entities.Permission;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.entities.Unit;
import uk.gov.digital.ho.hocs.info.repositories.UnitRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.logstash.logback.argument.StructuredArguments.value;
import static uk.gov.digital.ho.hocs.info.logging.LogEvent.*;

@Service
@Slf4j
public class BulkImportService {

    private Keycloak keycloakClient;
    private String hocsRealmName;

    private UnitRepository unitRepository;

    public BulkImportService(
            Keycloak keycloakClient,
            @Value("${keycloak.realm}") String hocsRealmName,
            UnitRepository unitRepository) {
        this.keycloakClient = keycloakClient;
        this.hocsRealmName = hocsRealmName;
        this.unitRepository = unitRepository;
    }

    public void refreshAllGroups() {
        log.info("Started Bulk Upload", value(EVENT, BULK_IMPORT_STARTED));


        PartialImportRepresentation keyCloakImport = new PartialImportRepresentation();
        List<GroupRepresentation> groups = new ArrayList<>();

        try {

            Set<Unit> units = unitRepository.findAll();
            for (Unit unit : units) {

                GroupRepresentation unitGroup = new GroupRepresentation();
                unitGroup.setSubGroups(new ArrayList<>());
                unitGroup.setName(unit.getShortCode());
                unitGroup.setPath(String.format("/%s", unit.getShortCode()));

                for (Team team : unit.getTeams()) {
                    GroupRepresentation teamGroup = new GroupRepresentation();
                    teamGroup.setSubGroups(new ArrayList<>());
                    teamGroup.setName(team.getUuid().toString());
                    teamGroup.setPath(String.format("/%s/%s", unit.getShortCode(), team.getUuid()));

                    for (Permission permission : team.getPermissions()) {

                        GroupRepresentation caseTypeGroup = new GroupRepresentation();
                        caseTypeGroup.setSubGroups(new ArrayList<>());
                        caseTypeGroup.setName(permission.getCaseType().getType());
                        caseTypeGroup.setPath(
                                String.format("/%s/%s/%s", unit.getShortCode(), team.getUuid(),
                                        permission.getCaseType().getType()));

                        GroupRepresentation permissionGroup = new GroupRepresentation();
                        permissionGroup.setName(permission.getAccessLevel().name());
                        permissionGroup.setPath(
                                String.format("/%s/%s/%s/%s", unit.getShortCode(), team.getUuid(),
                                        permission.getCaseType().getType(), permission.getAccessLevel().name()));

                        caseTypeGroup.getSubGroups().add(permissionGroup);
                        teamGroup.getSubGroups().add(caseTypeGroup);
                    }
                    unitGroup.getSubGroups().add(teamGroup);
                }
                groups.add(unitGroup);
                keyCloakImport.setGroups(groups);
            }
        } catch (Exception e) {
            log.error("Failed to generate partial import representation from database for reason: {}", e.getMessage(), value(EVENT, BULK_IMPORT_FAILURE));
            throw new BulkImportException("Failed to generate partial import representation from database", e);
        }

        try {
            keycloakClient.realm(hocsRealmName).partialImport(keyCloakImport);
        }
        catch (Exception e) {
            log.error("Failed to update keycloak with partial import for reason {}", e.getMessage(), value(EVENT, BULK_IMPORT_FAILURE));
            throw new BulkImportException("Failed to update keycloak with partial import", e);
        }
        log.info("Completed Bulk Upload", value(EVENT, BULK_IMPORT_SUCCESS));
    }


}
