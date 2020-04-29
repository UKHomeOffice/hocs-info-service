package uk.gov.digital.ho.hocs.info.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KeycloakServiceTest {

    private String HOCS_REALM = "hocs";

    private KeycloakService service;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TeamRepository teamRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    Keycloak keycloakClient;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    RealmResource hocsRealm;

    private UUID userUUID = UUID.randomUUID();

    @Before
    public void before() {
        service = new KeycloakService(teamRepository, keycloakClient, HOCS_REALM);
    }

    @Test
    public void shouldCallKeyCloakToAddUserToGroup() {
        UUID teamUUID = UUID.randomUUID();
        String teamUUIDPath = "/" + Base64UUID.uuidToBase64String(teamUUID);
        UserResource userResource = mock(UserResource.class);
        GroupRepresentation teamGroup = mock(GroupRepresentation.class);

        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.users().get(userUUID.toString())).thenReturn(userResource);
        when(hocsRealm.getGroupByPath(teamUUIDPath)).thenReturn(teamGroup);
        when(teamGroup.getId()).thenReturn("TEAM_GROUP");
        doNothing().when(userResource).joinGroup("TEAM_GROUP");

        service.addUserToTeam(userUUID, teamUUID);
        verify(userResource, times(1)).joinGroup("TEAM_GROUP");
    }

    @Test
    public void shouldCallKeyCloakToRemoveUserFromGroup() {
        UUID teamUUID = UUID.randomUUID();
        String teamUUIDPath = "/" + Base64UUID.uuidToBase64String(teamUUID);
        UserResource userResource = mock(UserResource.class);
        GroupRepresentation teamGroup = mock(GroupRepresentation.class);

        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.users().get(userUUID.toString())).thenReturn(userResource);
        when(hocsRealm.getGroupByPath(teamUUIDPath)).thenReturn(teamGroup);
        when(teamGroup.getId()).thenReturn("TEAM_GROUP");
        doNothing().when(userResource).leaveGroup("TEAM_GROUP");

        service.removeUserFromTeam(userUUID, teamUUID);
        verify(userResource, times(1)).leaveGroup("TEAM_GROUP");
    }

    @Test
    public void shouldCallKeycloakToCreateTeamGroup() {

        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.groups().add(any())).thenReturn(mock(Response.class));
        service.createTeamGroupIfNotExists(UUID.randomUUID());
        verify(hocsRealm.groups(), times(1)).add(any());
    }


    @Test
    public void shouldGetAllUsers() {

        List<UserRepresentation> userRepresentations = new ArrayList<>();
        UserRepresentation user = new UserRepresentation();
        user.setId(userUUID.toString());
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        userRepresentations.add(user);

        when(keycloakClient.realm(HOCS_REALM).users().count()).thenReturn(1);
        when(keycloakClient.realm(HOCS_REALM).users().list(0, 100)).thenReturn(userRepresentations);
        List<UserRepresentation> result = service.getAllUsers();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void shouldGetAllUsersBatchTest() {
        int expectedBatchFetchSize = 100;

        int batch1users = 100;
        int batch2users = 100;
        int batch3users = 100;
        int batch4users = 64;

        int totalNumberOfUsers = batch1users + batch2users + batch3users + batch4users;

        List<UserRepresentation> userRepresentations1 = createUserBatch(1, batch1users);
        List<UserRepresentation> userRepresentations2 = createUserBatch(2, batch2users);
        List<UserRepresentation> userRepresentations3 = createUserBatch(3, batch3users);
        List<UserRepresentation> userRepresentations4 = createUserBatch(4, batch4users);
        UsersResource usersResource = mock(UsersResource.class);
        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.users()).thenReturn(usersResource);
        when(usersResource.count()).thenReturn(totalNumberOfUsers);
        when(usersResource.list(0, expectedBatchFetchSize)).thenReturn(userRepresentations1);
        when(usersResource.list(batch1users, expectedBatchFetchSize)).thenReturn(userRepresentations2);
        when(usersResource.list(batch1users + batch2users, expectedBatchFetchSize)).thenReturn(userRepresentations3);
        when(usersResource.list(batch1users + batch2users + batch3users, expectedBatchFetchSize)).thenReturn(userRepresentations4);

        List<UserRepresentation> result = service.getAllUsers();

        assertThat(result.size()).isEqualTo(totalNumberOfUsers);
        assertThat(result.get(0).getFirstName()).isEqualTo("FirstNameBatch1-1");
        assertThat(result.get(55).getFirstName()).isEqualTo("FirstNameBatch1-56");
        assertThat(result.get(100).getFirstName()).isEqualTo("FirstNameBatch2-1");
        assertThat(result.get(127).getFirstName()).isEqualTo("FirstNameBatch2-28");
        assertThat(result.get(200).getFirstName()).isEqualTo("FirstNameBatch3-1");
        assertThat(result.get(299).getFirstName()).isEqualTo("FirstNameBatch3-100");
        assertThat(result.get(300).getFirstName()).isEqualTo("FirstNameBatch4-1");
        assertThat(result.get(363).getFirstName()).isEqualTo("FirstNameBatch4-64");

        verify(keycloakClient).realm(HOCS_REALM);
        verify(hocsRealm).users();
        verify(usersResource).count();
        verify(usersResource).list(0, expectedBatchFetchSize);
        verify(usersResource).list(batch1users, expectedBatchFetchSize);
        verify(usersResource).list(batch1users + batch2users, expectedBatchFetchSize);
        verify(usersResource).list(batch1users + batch2users + batch3users, expectedBatchFetchSize);

        verifyNoMoreInteractions(teamRepository, keycloakClient, hocsRealm, usersResource);
    }


    @Test
    public void shouldGetUserForUUID() {

        UserRepresentation user = new UserRepresentation();
        user.setId(userUUID.toString());
        user.setFirstName("FirstName");
        user.setLastName("LastName");

        when(keycloakClient.realm(HOCS_REALM).users().get(userUUID.toString()).toRepresentation()).thenReturn(user);
        UserRepresentation result = service.getUserFromUUID(userUUID);
        assertThat(result.getFirstName()).isEqualTo("FirstName");
        assertThat(result.getLastName()).isEqualTo("LastName");
        assertThat(result.getId()).isEqualTo(userUUID.toString());
    }

    @Test
    public void shouldGetGroupsOfUser() {

        UserRepresentation user = new UserRepresentation();
        user.setId(userUUID.toString());
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        List<GroupRepresentation> groups = new ArrayList<>();
        for (int i = 4; i > 0; i--) {
            GroupRepresentation groupRepresentation = new GroupRepresentation();
            UUID randomUUID = UUID.randomUUID();
            groupRepresentation.setId(Base64UUID.uuidToBase64String(randomUUID));
            groupRepresentation.setName(Base64UUID.uuidToBase64String(randomUUID));
            groups.add(groupRepresentation);
        }

        String userUUIDString = userUUID.toString();
        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.users().get(userUUIDString).groups()).thenReturn(groups);
        Set<UUID> result = service.getGroupsForUser(userUUID);

        assertThat(result).contains(Base64UUID.base64StringToUUID(groups.get(0).getId()));
        assertThat(result).contains(Base64UUID.base64StringToUUID(groups.get(1).getId()));
        assertThat(result).hasSize(4);
        verify(keycloakClient).realm(HOCS_REALM);
        verifyNoMoreInteractions(keycloakClient);
    }

    @Test
    public void GetGroupsOfUserShouldReturnAException() {
        UserRepresentation user = new UserRepresentation();
        user.setId(userUUID.toString());
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        NotFoundException mockException = mock(NotFoundException.class);
        String userUUIDString = userUUID.toString();
        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.users().get(userUUIDString).groups()).thenThrow(mockException);

        assertThatThrownBy(() -> service.getGroupsForUser(userUUID))
                .isInstanceOf(KeycloakException.class);
        verify(keycloakClient).realm(HOCS_REALM);
        verifyNoMoreInteractions(keycloakClient);
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

        String encodedTeamUUID = Base64UUID.uuidToBase64String(teamUUID);
        GroupRepresentation group = new GroupRepresentation() {
            {
                setPath("/" + encodedTeamUUID);
                setName(encodedTeamUUID);
                setId("1");
            }
        };
        when(keycloakClient.realm(HOCS_REALM).getGroupByPath("/" + encodedTeamUUID)).thenReturn(group);
        when(keycloakClient.realm(HOCS_REALM).groups().group("1").members()).thenReturn(userRepresentations);

        Set<UserRepresentation> result = service.getUsersForTeam(teamUUID);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void shouldGetEmptySetForTeamWithNoUsers() {
        UUID teamUUID = UUID.randomUUID();
        String encodedTeamUUIDPath = "/" + Base64UUID.uuidToBase64String(teamUUID);

        NotFoundException mockException = mock(NotFoundException.class);
        when(teamRepository.findByUuid(teamUUID)).thenReturn(new Team("Test Team", true));
        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.getGroupByPath(encodedTeamUUIDPath)).thenThrow(mockException);

        Set<UserRepresentation> result = service.getUsersForTeam(teamUUID);
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void shouldThrow404WhenSearchingForNonExistentTeam() {
        UUID teamUUID = UUID.randomUUID();
        String encodedTeamUUIDPath = "/" + Base64UUID.uuidToBase64String(teamUUID);

        when(teamRepository.findByUuid(teamUUID)).thenReturn(null);


        assertThatThrownBy(() -> service.getUsersForTeam(teamUUID))
                .isInstanceOf(ApplicationExceptions.EntityNotFoundException.class)
                .hasMessage("Team not found for UUID %s", teamUUID);
    }

    private List<UserRepresentation> createUserBatch(int batchNum, int usersToCreate) {
        List<UserRepresentation> userRepresentations = new ArrayList<>();
        for (int i = 0; i < usersToCreate; i++) {
            UserRepresentation user = new UserRepresentation();
            user.setId(UUID.randomUUID().toString());
            user.setFirstName("FirstNameBatch" + batchNum + "-" + (i + 1));
            user.setLastName("LastNameBatch" + batchNum + "-" + (i + 1));
            userRepresentations.add(user);
        }

        return userRepresentations;
    }
}