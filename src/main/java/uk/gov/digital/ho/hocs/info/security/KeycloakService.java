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

    public void addUserToGroup(UUID userUUID, String groupPath) {
        try {
            RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
            UserResource user = hocsRealm.users().get(userUUID.toString());
            GroupRepresentation group = hocsRealm.getGroupByPath(groupPath);
            user.joinGroup(group.getId());
        } catch (Exception e) {
            log.error("Failed to add user {} to group {} for reason: {}", userUUID, groupPath, e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public void createTeamGroupIfNotExists(String team) {
        try {
            RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
            GroupRepresentation teamGroup = new GroupRepresentation();
            teamGroup.setName(team);
            Response response = hocsRealm.groups().add(teamGroup);
            response.close();
        } catch (Exception e) {
            log.error("Failed to create group for team {} for reason: {}", team, e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public void deleteTeamPermisisons(String path) {
        try {
            String id = keycloakClient.realm(hocsRealmName).getGroupByPath(path).getId();
            keycloakClient.realm(hocsRealmName).groups().group(id).remove();

        } catch (Exception e) {
            log.error("Failed to delete permission group for with path {} for reason: {}", path, e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public void createGroupPathIfNotExists(String parentPath, String groupName) {
        try {
            RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
            GroupRepresentation parentGroup = hocsRealm.getGroupByPath(parentPath);
            GroupRepresentation newGroup = new GroupRepresentation();
            newGroup.setName(groupName);
            Response response = hocsRealm.groups().group(parentGroup.getId()).subGroup(newGroup);
            response.close();
        } catch (Exception e) {
            log.error("Failed to create group  {} with parent {} for reason: {}", groupName, parentPath, e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
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
        String encodedTeamUUID = Base64UUID.UUIDToBase64String(teamUUID);
        Map<String, UserRepresentation> members = new HashMap<>();
        List<GroupRepresentation> groups = keycloakClient.realm(hocsRealmName).groups().groups(encodedTeamUUID, 0, 1);
        groups.stream().flatMap(teamGroup -> teamGroup.getSubGroups().stream())
                        .flatMap(caseTypeGroup -> caseTypeGroup.getSubGroups().stream())
                        .map(permissionGroup -> keycloakClient.realm(hocsRealmName).groups().group(permissionGroup.getId()).members().stream()
                                .collect(Collectors.toMap(UserRepresentation::getId, u -> u))).forEach(members::putAll);

            return new HashSet<>(members.values());
    }

    public void updateUserTeamGroups(UUID teamUUID, Set<String> permissionPaths) {
        try {
            Set<UserRepresentation> users = getUsersForTeam(teamUUID);
            for (UserRepresentation user : users) {
                for (String path : permissionPaths) {
                    addUserToGroup(UUID.fromString(user.getId()), path);
                }
            }
        } catch (Exception e) {
            log.error("Failed to update Keycloak user groups for group {} for reason: {}. WARNING: User groups may now be out of sync.", teamUUID, e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }



}