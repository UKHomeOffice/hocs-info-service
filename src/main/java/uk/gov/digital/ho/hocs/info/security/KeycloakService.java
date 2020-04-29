package uk.gov.digital.ho.hocs.info.security;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserDto;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;

import javax.ws.rs.core.Response;
import java.util.*;

import static net.logstash.logback.argument.StructuredArguments.value;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.EVENT;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.KEYCLOAK_FAILURE;

@Service
@Slf4j
public class KeycloakService {

    private TeamRepository teamRepository;
    private Keycloak keycloakClient;
    private String hocsRealmName;

    private static final int USER_BATCH_FETCH_SIZE = 100;

    public KeycloakService(
            TeamRepository teamRepository,
            Keycloak keycloakClient,
            @Value("${keycloak.realm}") String hocsRealmName) {
        this.teamRepository = teamRepository;
        this.keycloakClient = keycloakClient;
        this.hocsRealmName = hocsRealmName;
    }

    public void createUser(CreateUserDto createUserDto) {
        try {
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(createUserDto.getPassword());
            credential.setTemporary(createUserDto.isTemporaryPassword());

            UserRepresentation user = new UserRepresentation();
            user.setUsername(createUserDto.getUsername());
            user.setFirstName(createUserDto.getFirstName());
            user.setLastName(createUserDto.getLastName());
            user.setEmail(createUserDto.getEmail());
            user.setCredentials(Arrays.asList(credential));
            user.setEnabled(true);
            user.setRealmRoles(createUserDto.getRealmRoles());

            Response result = keycloakClient.realm(hocsRealmName).users().create(user);
            if (result.getStatus() != 201) {
                log.error("Failed to create user {}, status {}", createUserDto.getUsername(), result.getStatus(), value(EVENT, KEYCLOAK_FAILURE));
                throw new KeycloakException(String.format("Failed to create user %s, status %s", createUserDto.getUsername(), result.getStatus()));
            }


            UUID userUUID = UUID.fromString(CreatedResponseUtil.getCreatedId(result));
            if (!CollectionUtils.isEmpty(createUserDto.getTeams())) {
                for (UUID teamUUID : createUserDto.getTeams()) {
                    addUserToTeam(userUUID, teamUUID);
                }
            }
        } catch (Exception e) {
            log.error("Failed to create user {}, exception {}", createUserDto.getUsername(), e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }

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

    @Retryable(maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.delay}"))
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


    @Retryable(maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.delay}"))
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

    @Retryable(maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.delay}"))
    public UserRepresentation getUserFromUUID(UUID userUUID) {
        return keycloakClient.realm(hocsRealmName).users().get(userUUID.toString()).toRepresentation();
    }

    @Retryable(maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.delay}"))
    public Set<UserRepresentation> getUsersForTeam(UUID teamUUID) {
        String encodedTeamPath = "/" + Base64UUID.uuidToBase64String(teamUUID);
        try {
            if (teamRepository.findByUuid(teamUUID) != null) {
                GroupRepresentation group = keycloakClient.realm(hocsRealmName).getGroupByPath(encodedTeamPath);
                return new HashSet<>(keycloakClient.realm(hocsRealmName).groups().group((group).getId()).members());
            } else {
                throw new ApplicationExceptions.EntityNotFoundException("Team not found for UUID %s", teamUUID);
            }
        } catch (javax.ws.rs.NotFoundException e) {
            log.error("Keycloak has not found users assigned to this team, Not found exception thrown by keycloak: " + e.getMessage());
            return Collections.emptySet();
        }
    }
}
