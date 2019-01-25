package uk.gov.digital.ho.hocs.info.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.GroupResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.*;

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
        String accessLevelGroupPath = "/TEAM/MIN/5";
        org.keycloak.admin.client.resource.UserResource userResource = mock(org.keycloak.admin.client.resource.UserResource.class);
        GroupRepresentation accessLevelGroup = mock(GroupRepresentation.class);

        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.users().get(userUUID.toString())).thenReturn(userResource);
        when(hocsRealm.getGroupByPath(accessLevelGroupPath)).thenReturn(accessLevelGroup);
        when(accessLevelGroup.getId()).thenReturn("ACCESS_GROUP");
        doNothing().when(userResource).joinGroup("ACCESS_GROUP");

        service = new KeycloakService(keycloakClient, HOCS_REALM);
        service.addUserToGroup(userUUID, accessLevelGroupPath);
        verify(userResource, times(1)).joinGroup("ACCESS_GROUP");
        ;

    }

    @Test
    public void shouldCallKeycloakToCreatUnitGroup() {

        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.groups().add(any())).thenReturn(mock(Response.class));
        service = new KeycloakService(keycloakClient, HOCS_REALM);
        service.createTeamGroupIfNotExists("TEAM");
        verify(hocsRealm.groups(), times(1)).add(any());
    }

    @Test
    public void shouldCreateGroupPathIfNotExists() {
        String groupPath = "TEAM";
        String groupId = "TEAM_GROUP";
        GroupRepresentation group = mock(GroupRepresentation.class);
        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(group.getId()).thenReturn(groupId);
        when(hocsRealm.getGroupByPath(groupPath)).thenReturn(group);

        service = new KeycloakService(keycloakClient, HOCS_REALM);
        service.createGroupPathIfNotExists(groupPath, "TEAM");

        verify(hocsRealm.groups().group(groupId), times(1)).subGroup(any());
    }

    @Test
    public void shouldGetAllUsers() {

        List<UserRepresentation> userRepresentations = new ArrayList<UserRepresentation>();
        UserRepresentation user = new UserRepresentation();
        user.setId(userUUID.toString());
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        userRepresentations.add(user);

        when(keycloakClient.realm(HOCS_REALM).users().list()).thenReturn(userRepresentations);
        service = new KeycloakService(keycloakClient, HOCS_REALM);
        List<UserRepresentation> result = service.getAllUsers();
        assertThat(result.size()).isEqualTo(1);
    }


    @Test
    public void shouldGetUserForUUID() {

        UserRepresentation user = new UserRepresentation();
        user.setId(userUUID.toString());
        user.setFirstName("FirstName");
        user.setLastName("LastName");

        when(keycloakClient.realm(HOCS_REALM).users().get(userUUID.toString()).toRepresentation()).thenReturn(user);
        service = new KeycloakService(keycloakClient, HOCS_REALM);
        UserRepresentation result = service.getUserFromUUID(userUUID);
        assertThat(result.getFirstName()).isEqualTo("FirstName");
        assertThat(result.getLastName()).isEqualTo("LastName");
        assertThat(result.getId()).isEqualTo(userUUID.toString());
    }

    @Test
    public void shouldGetUsersForTeam() {
        UUID teamUUID = UUID.randomUUID();
        List<UserRepresentation> userRepresentations = new ArrayList<>();
        UserRepresentation user = new UserRepresentation();
        user.setId(userUUID.toString());
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        userRepresentations.add(user);

        String encodedTeamUUID = Base64UUID.UUIDToBase64String(teamUUID);
        GroupRepresentation permissionGroup =  new GroupRepresentation() {
            {
                setName("5");
                setPath("/" + teamUUID + "/MIN/5");
            }};

        GroupRepresentation caseTypeGroup =  new GroupRepresentation() {
            {
                setId("GROUPID");
                setName("MIN");
                setPath("/" + teamUUID + "/MIN");

            }};

        caseTypeGroup.setSubGroups(Arrays.asList(permissionGroup));

        GroupRepresentation group = new GroupRepresentation(){
            {
                setPath("/" + teamUUID);
                setId(teamUUID.toString());
            }};
        group.setSubGroups(Arrays.asList(caseTypeGroup));


        List<GroupRepresentation> groups = Arrays.asList(group);

        when(keycloakClient.realm(HOCS_REALM).groups().groups(encodedTeamUUID, 0, 1)).thenReturn(groups);
        when(keycloakClient.realm(HOCS_REALM).groups().group(permissionGroup.getId()).members()).thenReturn(userRepresentations);;

        service = new KeycloakService(keycloakClient, HOCS_REALM);
        Set<UserRepresentation> result = service.getUsersForTeam(teamUUID);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void shouldUpdateGroupMemberPermissions() {
        UUID teamUUID = UUID.randomUUID();
        Set<String> permissions = new HashSet<String>() {{
            add("/" + teamUUID.toString() + "/CASETYPE1/5");
        }};

        Set<UserRepresentation> userRepresentations = new HashSet<>();
        UserRepresentation user = new UserRepresentation();
        user.setId(userUUID.toString());
        user.setFirstName("FirstName");
        user.setFirstName("LastName");
        userRepresentations.add(user);

        KeycloakService spyService = Mockito.spy(new KeycloakService(keycloakClient, HOCS_REALM));
        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        doReturn(userRepresentations).when(spyService).getUsersForTeam(teamUUID);
        doNothing().when(spyService).addUserToGroup(userUUID, "/" + teamUUID.toString() + "/CASETYPE1/5");

        spyService.updateUserTeamGroups(teamUUID, permissions);
        verify(spyService, times(1)).addUserToGroup(userUUID, "/" + teamUUID.toString() + "/CASETYPE1/5");

    }

}