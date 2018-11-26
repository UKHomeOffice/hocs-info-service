package uk.gov.digital.ho.hocs.info.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.GroupResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KeycloakServiceTest {


    private String HOCS_REALM = "hocs";

    private KeycloakService service;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    Keycloak keycloakClient;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    RealmResource hocsRealm;

    UUID userUUID = UUID.randomUUID();

    @Test
    public void shouldCallKeyCloakToAddUserToGroup() {
        String accessLevelGroupPath = "/UNIT/TEAMUUID/MIN/OWNER";
        org.keycloak.admin.client.resource.UserResource userResource = mock(org.keycloak.admin.client.resource.UserResource.class);
        GroupRepresentation accessLevelGroup = mock(GroupRepresentation.class);

        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.users().get(userUUID.toString())).thenReturn(userResource);
        when(hocsRealm.getGroupByPath(accessLevelGroupPath)).thenReturn(accessLevelGroup);
        when(accessLevelGroup.getId()).thenReturn("ACCESS_GROUP");
        doNothing().when(userResource).joinGroup("ACCESS_GROUP");

        service = new KeycloakService(keycloakClient, HOCS_REALM);
        service.addUserToGroup(userUUID, accessLevelGroupPath);
        verify(userResource, times(1)).joinGroup("ACCESS_GROUP");;

    }

    @Test
    public void shouldCallKeycloakToCreatUnitGroup() {

        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.groups().add(any())).thenReturn(mock(Response.class));
        service = new KeycloakService(keycloakClient, HOCS_REALM);
        service.createUnitGroupIfNotExists("UNIT");
        verify(hocsRealm.groups(), times(1)).add(any());
    }

    @Test
    public void shouldCreateGroupPathIfNotExists() {
        String unitGroupPath = "UNIT";
        String unitGroupId = "UNIT_GROUP";
        GroupRepresentation unitGroup = mock(GroupRepresentation.class);
        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(unitGroup.getId()).thenReturn(unitGroupId);
        when(hocsRealm.getGroupByPath(unitGroupPath)).thenReturn(unitGroup);

        service = new KeycloakService(keycloakClient, HOCS_REALM);
        service.createGroupPathIfNotExists(unitGroupPath, "TEAM");

        verify(hocsRealm.groups().group(unitGroupId), times(1)).subGroup(any());
    }

    @Test
    public void shouldGetAllUsers() {

        List<UserRepresentation> userRepresentations = new ArrayList<UserRepresentation>();
        UserRepresentation user =  new UserRepresentation();
        user.setId(userUUID.toString());
        user.setFirstName("FirstName");
        user.setFirstName("LastName");
        userRepresentations.add(new UserRepresentation());

        when(keycloakClient.realm(HOCS_REALM).users().list()).thenReturn(userRepresentations);
        service = new KeycloakService(keycloakClient, HOCS_REALM);
        List<UserRepresentation> result =service.getAllUsers();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void shouldMoveGroupToNewParent() {
        String currentGroupPath = "/CURRENT_UNIT/teamUUID";
        String newGroupPath = "/NEW_UNIT";


        GroupRepresentation oldGroup = new GroupRepresentation();
        oldGroup.setPath(currentGroupPath);
        oldGroup.setId("OLD_ID");

        GroupRepresentation newParentGroup = new GroupRepresentation();
        newParentGroup.setPath(newGroupPath);
        newParentGroup.setId("NEW_ID");

        GroupResource newParentGroupResource = mock(GroupResource.class);
        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.getGroupByPath(currentGroupPath)).thenReturn(oldGroup);
        when(hocsRealm.getGroupByPath(newGroupPath)).thenReturn(newParentGroup);
        when(hocsRealm.groups().group("NEW_ID")).thenReturn(newParentGroupResource);

        service = new KeycloakService(keycloakClient, HOCS_REALM);
        service.moveGroup(currentGroupPath, newGroupPath);

        verify(hocsRealm.groups().group("NEW_ID"), times(1)).subGroup(oldGroup);
    }


    @Test
    public void shouldUpdateGroupMemberPermissions() {
        String groupPath = "/UNIT1/teamUUID";

        GroupRepresentation group = new GroupRepresentation();
        group.setPath(groupPath);
        group.setId("GROUP_ID");

        GroupRepresentation caseTypeSubGroup = new GroupRepresentation();
        caseTypeSubGroup.setPath(groupPath + "/CASETYPE1");
        caseTypeSubGroup.setId("CASE_TYPE_GROUP_ID");

        GroupRepresentation permissionTypeSubGroup = new GroupRepresentation();
        permissionTypeSubGroup.setPath(groupPath + "/CASETYPE1" + "/OWNER");
        permissionTypeSubGroup.setId("PERMISSION_GROUP_ID");

        caseTypeSubGroup.setSubGroups(new ArrayList<GroupRepresentation>(){{
            add(permissionTypeSubGroup);
        }});

        group.setSubGroups(new ArrayList<GroupRepresentation>(){{
            add(caseTypeSubGroup);
        }});

        List<UserRepresentation> userRepresentations = new ArrayList<>();
        UserRepresentation user =  new UserRepresentation();
        user.setId(userUUID.toString());
        user.setFirstName("FirstName");
        user.setFirstName("LastName");
        userRepresentations.add(user);

        KeycloakService spyService = Mockito.spy(new KeycloakService(keycloakClient, HOCS_REALM));
        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        doNothing().when(spyService).addUserToGroup(userUUID, "/UNIT1/teamUUID/CASETYPE1/OWNER");
        when(hocsRealm.getGroupByPath(groupPath)).thenReturn(group);
        when(hocsRealm.groups().group(group.getId()).members()).thenReturn(userRepresentations);

        spyService.updateUserGroupsForGroup(groupPath);
        verify(spyService, times(1)).addUserToGroup(userUUID, "/UNIT1/teamUUID/CASETYPE1/OWNER");

    }

}