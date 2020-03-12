package uk.gov.digital.ho.hocs.info.security;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    public void addUserToTeam(UUID userUUID, UUID teamUUID) {
        try {
            String teamPath = "/" +  Base64UUID.UUIDToBase64String(teamUUID);
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
            String teamPath = "/" +  Base64UUID.UUIDToBase64String(teamUUID);
            RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
            UserResource user = hocsRealm.users().get(userUUID.toString());
            GroupRepresentation group = hocsRealm.getGroupByPath(teamPath);
            user.leaveGroup(group.getId());
        } catch (Exception e) {
            log.error("Failed to remove user {} from team {} for reason: {}, Event: {}", userUUID, teamUUID.toString(), e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public Set<UUID> getGroupsForUser(UUID userUUID) {
        try {
            RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
            List<GroupRepresentation> encodedTeamUUIDs = hocsRealm.users().get(userUUID.toString()).groups();
            Set<UUID> teamUUIDs = new HashSet<>();
            if (encodedTeamUUIDs.size() > 0) {
                encodedTeamUUIDs.forEach((group) -> teamUUIDs.add(Base64UUID.Base64StringToUUID(group.getName())));
            }
            return teamUUIDs;
        } catch (Exception e) {
            log.error("Failed to get groups for user {} for reason: {}, Event: {}", userUUID.toString(), e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public Set<String> getRolesForUser(UUID userUUID) {
        Set<String> roles  = new HashSet<>();
        try {
            RealmResource hocsRealm = keycloakClient.realm(hocsRealmName);
            List<RoleRepresentation> userRoles = hocsRealm.users().get(userUUID.toString()).roles().realmLevel().listEffective();
            if (userRoles.size() > 0) {
                userRoles.forEach((role) -> roles.add(role.getName()));
            }
        } catch (Exception e) {
            log.error("Failed to get roles for user {} for reason: {}, Event: {}", userUUID.toString(), e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }

        return roles;
    }

    public void createTeamGroupIfNotExists(UUID teamUUID) {
        try {
            String encodedTeamUUID = Base64UUID.UUIDToBase64String(teamUUID);
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
        String encodedTeamUUID = Base64UUID.UUIDToBase64String(teamUUID);
        String path = "/" + encodedTeamUUID;
        try {
            String id = keycloakClient.realm(hocsRealmName).getGroupByPath(path).getId();
            keycloakClient.realm(hocsRealmName).groups().group(id).remove();

        } catch (Exception e) {
            log.error("Failed to delete permission group for with path {} for reason: {}, Event: {}", path, e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
            throw new KeycloakException(e.getMessage(), e);
        }
    }

    public List<UserRepresentation> getAllUsers() {
        log.info("Get users from Keycloak realm {}", hocsRealmName);
        UsersResource usersResource = keycloakClient.realm(hocsRealmName).users();
        int totalUserCount = usersResource.count();
        List<UserRepresentation> users = new ArrayList<>();

        for(int i = 0; i < totalUserCount; i += USER_BATCH_FETCH_SIZE){
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
        String encodedTeamPath = "/" + Base64UUID.UUIDToBase64String(teamUUID);
        try {
            if (teamRepository.findByUuid(teamUUID) != null){
                GroupRepresentation group = keycloakClient.realm(hocsRealmName).getGroupByPath(encodedTeamPath);
                return new HashSet<>(keycloakClient.realm(hocsRealmName).groups().group((group).getId()).members());
            } else {
                throw new ApplicationExceptions.EntityNotFoundException("Team not found for UUID %s", teamUUID);
            }
        } catch (javax.ws.rs.NotFoundException e){
            log.error("Keycloak has not found users assigned to this team, Not found exception thrown by keycloak: " + e.getMessage());
            return Collections.emptySet();
        }
    }
}
