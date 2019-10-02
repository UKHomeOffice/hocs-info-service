package uk.gov.digital.ho.hocs.info.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
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

    @Test
    public void shouldCallKeyCloakToAddUserToGroup() {
        UUID teamUUID = UUID.randomUUID();
        String teamUUIDPath = "/" + Base64UUID.UUIDToBase64String(teamUUID);
        UserResource userResource = mock(UserResource.class);
        GroupRepresentation teamGroup = mock(GroupRepresentation.class);

        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.users().get(userUUID.toString())).thenReturn(userResource);
        when(hocsRealm.getGroupByPath(teamUUIDPath)).thenReturn(teamGroup);
        when(teamGroup.getId()).thenReturn("TEAM_GROUP");
        doNothing().when(userResource).joinGroup("TEAM_GROUP");

        service = new KeycloakService(teamRepository, keycloakClient, HOCS_REALM);
        service.addUserToTeam(userUUID, teamUUID);
        verify(userResource, times(1)).joinGroup("TEAM_GROUP");
    }

    @Test
    public void shouldCallKeyCloakToRemoveUserFromGroup() {
        UUID teamUUID = UUID.randomUUID();
        String teamUUIDPath = "/" + Base64UUID.UUIDToBase64String(teamUUID);
        UserResource userResource = mock(UserResource.class);
        GroupRepresentation teamGroup = mock(GroupRepresentation.class);

        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.users().get(userUUID.toString())).thenReturn(userResource);
        when(hocsRealm.getGroupByPath(teamUUIDPath)).thenReturn(teamGroup);
        when(teamGroup.getId()).thenReturn("TEAM_GROUP");
        doNothing().when(userResource).leaveGroup("TEAM_GROUP");

        service = new KeycloakService(teamRepository, keycloakClient, HOCS_REALM);
        service.removeUserFromTeam(userUUID, teamUUID);
        verify(userResource, times(1)).leaveGroup("TEAM_GROUP");
    }

    @Test
    public void shouldCallKeycloakToCreateTeamGroup() {

        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.groups().add(any())).thenReturn(mock(Response.class));
        service = new KeycloakService(teamRepository, keycloakClient, HOCS_REALM);
        service.createTeamGroupIfNotExists(UUID.randomUUID());
        verify(hocsRealm.groups(), times(1)).add(any());
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
        service = new KeycloakService(teamRepository, keycloakClient, HOCS_REALM);
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
        service = new KeycloakService(teamRepository, keycloakClient, HOCS_REALM);
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
        GroupRepresentation group = new GroupRepresentation(){
            {
                setPath("/" + encodedTeamUUID);
                setName(encodedTeamUUID);
                setId("1");
            }};
        when(keycloakClient.realm(HOCS_REALM).getGroupByPath("/" + encodedTeamUUID)).thenReturn(group);
        when(keycloakClient.realm(HOCS_REALM).groups().group("1").members()).thenReturn(userRepresentations);

        service = new KeycloakService(teamRepository, keycloakClient, HOCS_REALM);
        Set<UserRepresentation> result = service.getUsersForTeam(teamUUID);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void shouldGetEmptySetForTeamWithNoUsers() {
        UUID teamUUID = UUID.randomUUID();
        String encodedTeamUUIDPath = "/" + Base64UUID.UUIDToBase64String(teamUUID);

        NotFoundException mockException = mock(NotFoundException.class);
        when(teamRepository.findByUuid(teamUUID)).thenReturn(new Team("Test Team", true));
        when(keycloakClient.realm(HOCS_REALM)).thenReturn(hocsRealm);
        when(hocsRealm.getGroupByPath(encodedTeamUUIDPath)).thenThrow(mockException);

        service = new KeycloakService(teamRepository, keycloakClient, HOCS_REALM);
        Set<UserRepresentation> result = service.getUsersForTeam(teamUUID);
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void shouldThrow404WhenSearchingForNonExistentTeam() {
        UUID teamUUID = UUID.randomUUID();
        String encodedTeamUUIDPath = "/" + Base64UUID.UUIDToBase64String(teamUUID);

        when(teamRepository.findByUuid(teamUUID)).thenReturn(null);

        service = new KeycloakService(teamRepository, keycloakClient, HOCS_REALM);

        assertThatThrownBy(() -> service.getUsersForTeam(teamUUID))
                .isInstanceOf(ApplicationExceptions.EntityNotFoundException.class)
                .hasMessage("Team not found for UUID %s", teamUUID);
    }
}