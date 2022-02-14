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
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.GroupResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.util.JsonSerialization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserResponse;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateUserDto;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;

@Service
@Slf4j
public class KeycloakService {

    private final TeamRepository teamRepository;
    private final RealmResource keycloakRealm;

    private static final int USER_BATCH_FETCH_SIZE = 100;
    private static final String KEYCLOAK_ERROR_MESSAGE = "errorMessage";

    public KeycloakService(
            TeamRepository teamRepository,
            Keycloak keycloakClient,
            @Value("${keycloak.realm}") String hocsRealmName) {
        this.teamRepository = teamRepository;
        this.keycloakRealm = keycloakClient.realm(hocsRealmName);
    }

    public CreateUserResponse createUser(CreateUserDto createUserDto) {

        UsersResource usersResource = keycloakRealm.users();
        if (usersResource.search(createUserDto.getEmail()).size() > 0) {
            log.warn("User {} creation failed - email already exists", createUserDto.getEmail(),
                    value(EVENT, KEYCLOAK_FAILURE));
            throw new ApplicationExceptions.UserAlreadyExistsException();
        }

        UserRepresentation userRepresentation = mapToUserRepresentation(createUserDto);

        Response response = usersResource.create(userRepresentation);
        if (response.getStatus() != HttpStatus.SC_CREATED) {
            String errorMessage = extractCreateUserErrorMessage(response.getEntity());
            log.error("Failed to create user {}, status {}", createUserDto.getEmail(), response.getStatus(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(errorMessage, response.getStatus());
        }

        String userId = CreatedResponseUtil.getCreatedId(response);
        return new CreateUserResponse(userId);
    }

    public void updateUser(UUID userUUID, UpdateUserDto updateUserDto) {

        UserResource userResource = keycloakRealm.users().get(userUUID.toString());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setFirstName(updateUserDto.getFirstName());
        userRepresentation.setLastName(updateUserDto.getLastName());
        userRepresentation.setEnabled(updateUserDto.getEnabled());
        userResource.update(userRepresentation);
    }

    public void addUserToTeam(UUID userUUID, UUID teamUUID) {
        try {
            String teamPath = "/" + Base64UUID.uuidToBase64String(teamUUID);
            UserResource user = keycloakRealm.users().get(userUUID.toString());
            GroupRepresentation group = keycloakRealm.getGroupByPath(teamPath);
            user.joinGroup(group.getId());
        } catch (Exception e) {
            log.error("Failed to add user {} to team {} for reason: {}, Event: {}", userUUID, teamUUID.toString(), e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public void removeUserFromTeam(UUID userUUID, UUID teamUUID) {
        try {
            String teamPath = "/" + Base64UUID.uuidToBase64String(teamUUID);
            UserResource user = keycloakRealm.users().get(userUUID.toString());
            GroupRepresentation group = keycloakRealm.getGroupByPath(teamPath);
            user.leaveGroup(group.getId());
        } catch (Exception e) {
            log.error("Failed to remove user {} from team {} for reason: {}, Event: {}", userUUID, teamUUID.toString(), e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public void createTeamGroupIfNotExists(UUID teamUUID) {
        try {
            String encodedTeamUUID = Base64UUID.uuidToBase64String(teamUUID);
            GroupRepresentation teamGroup = new GroupRepresentation();
            teamGroup.setName(encodedTeamUUID);
            Response response = keycloakRealm.groups().add(teamGroup);
            response.close();
        } catch (Exception e) {
            log.error("Failed to create group for team {} for reason: {}, Event: {}", teamUUID.toString(), e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public Set<UUID> getUserTeams(UUID userUUID) {
        try {
            List<GroupRepresentation> encodedTeamUUIDs = keycloakRealm.users().get(userUUID.toString()).groups();
            return encodedTeamUUIDs.stream().map(group -> Base64UUID.base64StringToUUID(group.getName())).collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("Failed to get groups for user {} for reason: {}, Event: {}", userUUID.toString(), e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public List<UserRepresentation> getAllUsers() {
        UsersResource usersResource = keycloakRealm.users();
        List<UserRepresentation> users = new ArrayList<>();
        var count = usersResource.count();
        for (int i = 0; i < count; i += USER_BATCH_FETCH_SIZE) {
            users.addAll(usersResource.list(i, USER_BATCH_FETCH_SIZE));
        }

        log.info("Found {} users in Keycloak", users.size());
        return users;
    }

    public UserRepresentation getUserFromUUID(UUID userUUID) {
        return keycloakRealm.users().get(userUUID.toString()).toRepresentation();
    }

    @Cacheable(value = "getUsersForTeam")
    public Set<UserRepresentation> getUsersForTeam(UUID teamUUID) {
        log.info("Get users for team {}", teamUUID);

        if (teamRepository.findByUuid(teamUUID) != null) {
            try {
                GroupRepresentation group = keycloakRealm.getGroupByPath("/" + Base64UUID.uuidToBase64String(teamUUID));
                GroupResource groupResource = keycloakRealm.groups().group(group.getId());
                Set<UserRepresentation> groupUsers = new HashSet<>();
                do {
                    List<UserRepresentation> pageUsers = groupResource.members(groupUsers.size(), USER_BATCH_FETCH_SIZE);
                    groupUsers.addAll(pageUsers);

                    if (pageUsers.size() < USER_BATCH_FETCH_SIZE) {
                        break;
                    }
                } while (groupUsers.size() != 0);

                return groupUsers;

            } catch (javax.ws.rs.NotFoundException e) {
                log.error("Keycloak has not found users assigned to this team, Not found exception thrown by keycloak: " + e.getMessage());
                return new HashSet<>();
            }

        } else {
           throw new ApplicationExceptions.EntityNotFoundException("Team not found for UUID %s", teamUUID);
        }
    }

    private String extractCreateUserErrorMessage(Object entity) {
        if (entity instanceof FilterInputStream) {
            FilterInputStream filterInputStream = (FilterInputStream) entity;
            try {
                var error = JsonSerialization.readValue(filterInputStream, Map.class);
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
}
