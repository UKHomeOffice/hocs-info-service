package uk.gov.digital.ho.hocs.info.security;

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

@Service
public class KeycloakService {

    private Keycloak keycloakClient;
    private String hocsRealmName;

    public KeycloakService(
            Keycloak keycloakClient,
            @Value("${keycloak.realm}") String hocsRealmName) {
        this.keycloakClient = keycloakClient;
        this.hocsRealmName = hocsRealmName;
    }


   public  void addUserToGroup(UUID userUUID, String groupPath) {
        RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
        UserResource user = hocsRealm.users().get(userUUID.toString());
        GroupRepresentation group = hocsRealm.getGroupByPath(groupPath);
        user.joinGroup(group.getId());
    }

    public void createUnitGroupIfNotExists(String unit) {
        RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
        GroupRepresentation unitGroup = new GroupRepresentation();
        unitGroup.setName(unit);
        Response response = hocsRealm.groups().add(unitGroup);
        response.close();
    }

    public void createGroupPathIfNotExists(String parentPath, String groupName) {
        RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
        GroupRepresentation parentGroup =  hocsRealm.getGroupByPath(parentPath);
        GroupRepresentation newGroup = new GroupRepresentation();
        newGroup.setName(groupName);
        System.out.println(parentGroup.getId());
        Response response = hocsRealm.groups().group(parentGroup.getId()).subGroup(newGroup);
        response.close();
    }

    public List<UserRepresentation> getAllUsers() {
        List<UserRepresentation> users = keycloakClient.realm(hocsRealmName).users().list();
        return users;
    }

    public void moveGroup(String currentGroupPath, String newParent) {
        RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
        GroupRepresentation group = hocsRealm.getGroupByPath(currentGroupPath);
        GroupRepresentation newParentGroup = hocsRealm.getGroupByPath(newParent);
        hocsRealm.groups().group(newParentGroup.getId()).subGroup(group);

    }

}
