package uk.gov.digital.ho.hocs.info.security;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.value;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.EVENT;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.KEYCLOAK_FAILURE;

@Service
@Slf4j
public class KeycloakService {

    private Keycloak keycloakClient;
    private String hocsRealmName;

    public KeycloakService(
            Keycloak keycloakClient,
            @Value("${keycloak.realm}") String hocsRealmName) {
        this.keycloakClient = keycloakClient;
        this.hocsRealmName = hocsRealmName;
    }

    public void addUserToTeam(UUID userUUID, UUID teamUUID) {
        try {
            String teamPath = "/" +  Base64UUID.UUIDToBase64String(teamUUID);
            RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
            UserResource user = hocsRealm.users().get(userUUID.toString());
            GroupRepresentation group = hocsRealm.getGroupByPath(teamPath);
            user.joinGroup(group.getId());
        } catch (Exception e) {
            log.error("Failed to add user {} to team {} for reason: {}", userUUID, teamUUID.toString(), e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public void removeUserFromTeam(UUID userUUID, UUID teamUUID) {
        try {
            String teamPath = "/" +  Base64UUID.UUIDToBase64String(teamUUID);
            RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
            UserResource user = hocsRealm.users().get(userUUID.toString());
            GroupRepresentation group = hocsRealm.getGroupByPath(teamPath);
            user.leaveGroup(group.getId());
        } catch (Exception e) {
            log.error("Failed to remove user {} from team {} for reason: {}", userUUID, teamUUID.toString(), e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public void createTeamGroupIfNotExists(UUID teamUUID) {
        try {
            String encodedTeamUUID = Base64UUID.UUIDToBase64String(teamUUID);
            RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
            GroupRepresentation teamGroup = new GroupRepresentation();
            teamGroup.setName(encodedTeamUUID);
            Response response = hocsRealm.groups().add(teamGroup);
            response.close();
        } catch (Exception e) {
            log.error("Failed to create group for team {} for reason: {}", teamUUID.toString(), e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public void deleteTeamGroup(UUID teamUUID) {
        String encodedTeamUUID = Base64UUID.UUIDToBase64String(teamUUID);
        String path = "/" + encodedTeamUUID;
        try {
            String id = keycloakClient.realm(hocsRealmName).getGroupByPath(path).getId();
            keycloakClient.realm(hocsRealmName).groups().group(id).remove();

        } catch (Exception e) {
            log.error("Failed to delete permission group for with path {} for reason: {}", path, e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public List<UserRepresentation> getAllUsers() {
        return keycloakClient.realm(hocsRealmName).users().list();
    }

    public UserRepresentation getUserFromUUID(UUID userUUID) {
        return keycloakClient.realm(hocsRealmName).users().get(userUUID.toString()).toRepresentation();
    }

    public Set<UserRepresentation> getUsersForTeam(UUID teamUUID) {
        String encodedTeamPath = "/" + Base64UUID.UUIDToBase64String(teamUUID);
        GroupRepresentation group = keycloakClient.realm(hocsRealmName).getGroupByPath(encodedTeamPath);
        return keycloakClient.realm(hocsRealmName).groups().group((group).getId()).members().stream().collect(Collectors.toSet());
    }

}