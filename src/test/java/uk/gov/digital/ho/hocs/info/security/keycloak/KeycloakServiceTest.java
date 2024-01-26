package uk.gov.digital.ho.hocs.info.security.keycloak;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserResponse;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateUserDto;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;
import uk.gov.digital.ho.hocs.info.security.Base64UUID;
import uk.gov.digital.ho.hocs.info.security.KeycloakException;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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

        assertThatThrownBy(() -> service.getGroupsForUser(userUUID)).isInstanceOf(KeycloakException.class);
        verify(keycloakClient).realm(HOCS_REALM);
        verifyNoMoreInteractions(keycloakClient);
    }

    @Test
    public void shouldGetEmptySetForTeamWithNoUsers() {
        UUID teamUUID = UUID.randomUUID();
        String encodedTeamUUIDPath = "/" + Base64UUID.uuidToBase64String(teamUUID);

        NotFoundException mockException = mock(NotFoundException.class);
        when(teamRepository.findByUuid(teamUUID)).thenReturn(new Team("Test Team", true));
        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.getGroupByPath(encodedTeamUUIDPath)).thenThrow(mockException);

        List<UserRepresentation> result = service.getUsersForTeam(teamUUID);
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void shouldThrow404WhenSearchingForNonExistentTeam() {
        UUID teamUUID = UUID.randomUUID();

        when(teamRepository.findByUuid(teamUUID)).thenReturn(null);

        assertThatThrownBy(() -> service.getUsersForTeam(teamUUID)).isInstanceOf(
            ApplicationExceptions.EntityNotFoundException.class).hasMessage("Team not found for UUID %s", teamUUID);
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

    @Test
    public void shouldCreateUser() {

        //given
        String UUID = java.util.UUID.randomUUID().toString();
        CreateUserDto createUserDto = new CreateUserDto("email", "firstname", "lastname");
        UsersResource usersResource = mock(UsersResource.class);
        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.users()).thenReturn(usersResource);
        Response response = mock(Response.class);
        when(response.getLocation()).thenReturn(URI.create("/path/" + UUID));
        when(response.getStatusInfo()).thenReturn(Response.Status.CREATED);
        when(response.getStatus()).thenReturn(HttpStatus.SC_CREATED);
        when(usersResource.create(any())).thenReturn(response);

        //when
        CreateUserResponse createUserResponse = service.createUser(createUserDto);

        //then
        assertThat(createUserResponse.getUserUUID()).isEqualTo(UUID);
        ArgumentCaptor<UserRepresentation> userRepresentationCaptor = ArgumentCaptor.forClass(UserRepresentation.class);
        verify(usersResource).create(userRepresentationCaptor.capture());
        UserRepresentation userRepresentation = userRepresentationCaptor.getValue();
        assertThat(userRepresentation.getEmail()).isEqualTo("email");
        assertThat(userRepresentation.getFirstName()).isEqualTo("firstname");
        assertThat(userRepresentation.getLastName()).isEqualTo("lastname");
    }

    @Test
    public void shouldThrowKeycloakExceptionWithStatusCodeIfUserCreationFails() {

        //given
        String errorMessage = "xyz";
        CreateUserDto createUserDto = new CreateUserDto("email", "firstname", "lastname");
        UsersResource usersResource = mock(UsersResource.class);
        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.users()).thenReturn(usersResource);
        Response response = mock(Response.class);
        String mapString = "{ \"errorMessage\" : \"" + errorMessage + "\" }";
        FilterInputStream fis = new BufferedInputStream(new ByteArrayInputStream(mapString.getBytes()));
        when(response.getEntity()).thenReturn(fis);
        when(response.getStatus()).thenReturn(HttpStatus.SC_BAD_REQUEST);
        when(usersResource.create(any())).thenReturn(response);

        //when & then
        assertThatThrownBy(() -> service.createUser(createUserDto)).isInstanceOf(KeycloakException.class).hasMessage(
            errorMessage);
    }

    @Test
    public void shouldUpdateUser() {

        //given
        UUID userUUID = UUID.randomUUID();
        UpdateUserDto updateUserDto = new UpdateUserDto("firstname", "lastname", true);
        UsersResource usersResource = mock(UsersResource.class);
        UserResource userResource = mock(UserResource.class);
        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.users()).thenReturn(usersResource);
        when(usersResource.get(userUUID.toString())).thenReturn(userResource);
        UserRepresentation userRepresentation = new UserRepresentation();
        when(userResource.toRepresentation()).thenReturn(userRepresentation);

        //when
        service.updateUser(userUUID, updateUserDto);

        //then
        ArgumentCaptor<UserRepresentation> userRepresentationCaptor = ArgumentCaptor.forClass(UserRepresentation.class);
        verify(userResource).update(userRepresentationCaptor.capture());
        UserRepresentation newUserRepresentation = userRepresentationCaptor.getValue();
        assertThat(newUserRepresentation.getFirstName()).isEqualTo(updateUserDto.getFirstName());
        assertThat(newUserRepresentation.getLastName()).isEqualTo(updateUserDto.getLastName());
        assertThat(newUserRepresentation.isEnabled()).isEqualTo(updateUserDto.getEnabled());
    }

}
