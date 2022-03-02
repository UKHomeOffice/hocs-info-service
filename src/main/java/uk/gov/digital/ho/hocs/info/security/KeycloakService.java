package uk.gov.digital.ho.hocs.info.security;

import static net.logstash.logback.argument.StructuredArguments.value;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.EVENT;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.KEYCLOAK_FAILURE;

import java.io.FilterInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.util.JsonSerialization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserResponse;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateUserDto;
import uk.gov.digital.ho.hocs.info.application.LogEvent;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;

@Service
@Slf4j
public class KeycloakService {

    private TeamRepository teamRepository;
    private Keycloak keycloakClient;
    private String hocsRealmName;

    private static final int USER_BATCH_FETCH_SIZE = 100;
    private static final String KEYCLOAK_ERROR_MESSAGE = "errorMessage";

    public KeycloakService(
            TeamRepository teamRepository,
            Keycloak keycloakClient,
            @Value("${keycloak.realm}") String hocsRealmName) {
        this.teamRepository = teamRepository;
        this.keycloakClient = keycloakClient;
        this.hocsRealmName = hocsRealmName;
    }

    public CreateUserResponse createUser(CreateUserDto createUserDto) {

        UsersResource usersResource = keycloakClient.realm(hocsRealmName).users();
        List<UserRepresentation> existingUserReps = usersResource.search(createUserDto.getEmail());

        if (!existingUserReps.isEmpty()) {
            log.info("User with email already exists as user {}",
                existingUserReps.get(0).getId(),
                value(EVENT, LogEvent.CREATE_USER_FAILED)
            );

            throw new ApplicationExceptions.UserAlreadyExistsException("User with provided email address already exists", LogEvent.CREATE_USER_FAILED);
        }

        UserRepresentation userRepresentation = mapToUserRepresentation(createUserDto);
        Response response = usersResource.create(userRepresentation);

        if (response.getStatus() != HttpStatus.SC_CREATED) {
            String errorMessage = extractCreateUserErrorMessage(response.getEntity());
            log.error("Failed to create user, status {}", response.getStatus(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(errorMessage, response.getStatus());
        }

        String userId = CreatedResponseUtil.getCreatedId(response);
        log.info("Created new user {}", userId, value(EVENT, LogEvent.CREATE_USER_SUCCESS));

        return new CreateUserResponse(userId);
    }

    public void updateUser(UUID userUUID, UpdateUserDto updateUserDto) {

        UserResource userResource = keycloakClient.realm(hocsRealmName).users().get(userUUID.toString());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setFirstName(updateUserDto.getFirstName());
        userRepresentation.setLastName(updateUserDto.getLastName());
        userRepresentation.setEnabled(updateUserDto.getEnabled());
        userResource.update(userRepresentation);
    }

    public void addUserToTeam(UUID userUUID, UUID teamUUID) {
        try {
            String teamPath = "/" + Base64UUID.uuidToBase64String(teamUUID);
            RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
            UserResource user = hocsRealm.users().get(userUUID.toString());
            GroupRepresentation group = hocsRealm.getGroupByPath(teamPath);
            user.joinGroup(group.getId());
        } catch (Exception e) {
            log.error("Failed to add user {} to team {} for reason: {}, Event: {}", userUUID, teamUUID.toString(), e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public void removeUserFromTeam(UUID userUUID, UUID teamUUID) {
        try {
            String teamPath = "/" + Base64UUID.uuidToBase64String(teamUUID);
            RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
            UserResource user = hocsRealm.users().get(userUUID.toString());
            GroupRepresentation group = hocsRealm.getGroupByPath(teamPath);
            user.leaveGroup(group.getId());
        } catch (Exception e) {
            log.error("Failed to remove user {} from team {} for reason: {}, Event: {}", userUUID, teamUUID.toString(), e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public void createTeamGroupIfNotExists(UUID teamUUID) {
        try {
            String encodedTeamUUID = Base64UUID.uuidToBase64String(teamUUID);
            RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
            GroupRepresentation teamGroup = new GroupRepresentation();
            teamGroup.setName(encodedTeamUUID);
            Response response = hocsRealm.groups().add(teamGroup);
            response.close();
        } catch (Exception e) {
            log.error("Failed to create group for team {} for reason: {}, Event: {}", teamUUID.toString(), e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public void deleteTeamGroup(UUID teamUUID) {
        String encodedTeamUUID = Base64UUID.uuidToBase64String(teamUUID);
        String path = "/" + encodedTeamUUID;
        try {
            String id = keycloakClient.realm(hocsRealmName).getGroupByPath(path).getId();
            keycloakClient.realm(hocsRealmName).groups().group(id).remove();

        } catch (Exception e) {
            log.error("Failed to delete permission group for with path {} for reason: {}, Event: {}", path, e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public Set<UUID> getGroupsForUser(UUID userUUID) {
        try {
            RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
            List<GroupRepresentation> encodedTeamUUIDs = hocsRealm.users().get(userUUID.toString()).groups();
            Set<UUID> teamUUIDs = new HashSet<>();
            if (!encodedTeamUUIDs.isEmpty()) {
                encodedTeamUUIDs.forEach(group -> teamUUIDs.add(Base64UUID.base64StringToUUID(group.getName())));
            }
            return teamUUIDs;
        } catch (Exception e) {
            log.error("Failed to get groups for user {} for reason: {}, Event: {}", userUUID.toString(), e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }


    public List<UserRepresentation> getAllUsers() {
        log.info("Get users from Keycloak realm {}", hocsRealmName);
        UsersResource usersResource = keycloakClient.realm(hocsRealmName).users();
        int totalUserCount = usersResource.count();
        List<UserRepresentation> users = new ArrayList<>();

        for (int i = 0; i < totalUserCount; i += USER_BATCH_FETCH_SIZE) {
            log.debug("Batch fetching users, total user count: {}, fetched so far: {}", totalUserCount, i);
            users.addAll(usersResource.list(i, USER_BATCH_FETCH_SIZE));
        }

        log.info("Found {} users in Keycloak", users.size());
        return users;
    }

    public UserRepresentation getUserFromUUID(UUID userUUID) {
        return keycloakClient.realm(hocsRealmName).users().get(userUUID.toString()).toRepresentation();
    }

    public Set<UserRepresentation> getUsersForTeam(UUID teamUUID) {
        String encodedTeamPath = "/" + Base64UUID.uuidToBase64String(teamUUID);
        HashSet<UserRepresentation> groupUsers = new HashSet<>();
        try {
            if (teamRepository.findByUuid(teamUUID) != null) {
                GroupRepresentation group = keycloakClient.realm(hocsRealmName).getGroupByPath(encodedTeamPath);

                do {
                    List<UserRepresentation> pageUsers =
                            keycloakClient.realm(hocsRealmName)
                                    .groups().group((group).getId())
                                    .members(groupUsers.size(), USER_BATCH_FETCH_SIZE);
                    groupUsers.addAll(pageUsers);

                    if (pageUsers.size() < USER_BATCH_FETCH_SIZE) {
                        break;
                    }
                } while (groupUsers.size() != 0);

                return groupUsers;
            } else {
               throw new ApplicationExceptions.EntityNotFoundException("Team not found for UUID %s", teamUUID);
            }
        } catch (javax.ws.rs.NotFoundException e) {
            log.error("Keycloak has not found users assigned to this team, Not found exception thrown by keycloak: " + e.getMessage());
            return groupUsers;
        }
    }

    private String extractCreateUserErrorMessage(Object entity) {
        if (entity instanceof FilterInputStream) {
            FilterInputStream filterInputStream = (FilterInputStream) entity;
            try {
                Map error = JsonSerialization.readValue(filterInputStream, Map.class);
                return (String) error.get(KEYCLOAK_ERROR_MESSAGE);
            } catch (IOException e) {
                log.warn("Could not parse error message");
            }
        }
        return "";
    }

    private UserRepresentation mapToUserRepresentation(CreateUserDto createUserDto) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(createUserDto.getEmail());
        userRepresentation.setUsername(createUserDto.getEmail());
        userRepresentation.setFirstName(createUserDto.getFirstName());
        userRepresentation.setLastName(createUserDto.getLastName());
        userRepresentation.setEnabled(true);
        return userRepresentation;
    }

    public void refreshCache() {
        keycloakClient.realm(hocsRealmName).clearRealmCache();
    }
}
