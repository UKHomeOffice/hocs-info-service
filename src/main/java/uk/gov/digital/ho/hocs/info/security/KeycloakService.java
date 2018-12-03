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
import java.util.List;
import java.util.UUID;
import static net.logstash.logback.argument.StructuredArguments.value;
import static uk.gov.digital.ho.hocs.info.logging.LogEvent.EVENT;
import static uk.gov.digital.ho.hocs.info.logging.LogEvent.TEAM_CREATED;

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
        }
        catch(Exception e) {
            log.error("Failed to add user {} to group {} for reason: {}", userUUID, groupPath, e.getMessage() , value(EVENT, TEAM_CREATED));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public void createUnitGroupIfNotExists(String unit) {
        try {
            RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
            GroupRepresentation unitGroup = new GroupRepresentation();
            unitGroup.setName(unit);
            Response response = hocsRealm.groups().add(unitGroup);
            response.close();
        }
        catch(Exception e) {
            log.error("Failed to create group for unit {} for reason: {}", unit, e.getMessage() , value(EVENT, TEAM_CREATED));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public void createGroupPathIfNotExists(String parentPath, String groupName) {
      try {
        RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
        GroupRepresentation parentGroup =  hocsRealm.getGroupByPath(parentPath);
        GroupRepresentation newGroup = new GroupRepresentation();
        newGroup.setName(groupName);
        Response response = hocsRealm.groups().group(parentGroup.getId()).subGroup(newGroup);
        response.close();
      }
      catch(Exception e) {
          log.error("Failed to create group  {} with parent {} for reason: {}", groupName ,parentPath, e.getMessage() , value(EVENT, TEAM_CREATED));
          throw new KeycloakException(e.getMessage(), e);
      }
    }

    public List<UserRepresentation> getAllUsers() {
        List<UserRepresentation> users = keycloakClient.realm(hocsRealmName).users().list();
        return users;
    }

    public List<UserRepresentation> getUsersForTeam(String path, String teamUUID) {
        String id = keycloakClient.realm(hocsRealmName).getGroupByPath(path).getId();
        List<UserRepresentation> users = keycloakClient.realm(hocsRealmName).groups().group(id).members();
        return users;
    }

    public void updateUserGroupsForGroup(String groupPath) {
        try {
            RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
            GroupRepresentation group = hocsRealm.getGroupByPath(groupPath);
            List<UserRepresentation> users = hocsRealm.groups().group(group.getId()).members();
            for (GroupRepresentation subGroup : group.getSubGroups()) {
                for (GroupRepresentation permissionGroup : subGroup.getSubGroups()) {
                    for (UserRepresentation user : users) {
                        addUserToGroup(UUID.fromString(user.getId()), permissionGroup.getPath());
                    }
                }
            }
        }
        catch(Exception e) {
            log.error("Failed to update Keycloak user groups for group {} for reason: {}. WARNING: User groups may now be out of sync." ,groupPath, e.getMessage() , value(EVENT, TEAM_CREATED));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public void moveGroup(String currentGroupPath, String newParent) {
        try {
            RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
            GroupRepresentation group = hocsRealm.getGroupByPath(currentGroupPath);
            createUnitGroupIfNotExists(newParent);
            GroupRepresentation newParentGroup = hocsRealm.getGroupByPath(newParent);
            hocsRealm.groups().group(newParentGroup.getId()).subGroup(group);
        }
        catch(Exception e) {
            log.error("Failed to move Keycloak group {} to new parent {} for reason: {}." ,currentGroupPath, newParent, e.getMessage() , value(EVENT, TEAM_CREATED));
            throw new KeycloakException(e.getMessage(), e);
        }
    }
}