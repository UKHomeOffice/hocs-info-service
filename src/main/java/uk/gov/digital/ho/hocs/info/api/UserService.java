package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.UserDto;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UserService {

    private KeycloakService keycloakService;
    private CaseworkClient caseworkClient;

    @Autowired
    public UserService(KeycloakService keycloakService, CaseworkClient caseworkClient) {
        this.keycloakService = keycloakService;

        this.caseworkClient = caseworkClient;
    }

    @Cacheable("users")
    public List<UserDto> getAllUsers() {
        return keycloakService.getAllUsers().stream().map(user -> UserDto.from(user)).collect(Collectors.toList());
    }

    @CachePut("users")
    public List<UserDto> refreshUserCache() {
        return keycloakService.getAllUsers().stream().map(user -> UserDto.from(user)).collect(Collectors.toList());
    }

    public UserDto getUserByUUID(UUID userUUID) {
        return UserDto.from(keycloakService.getUserFromUUID(userUUID));
    }

    @Cacheable(value = "teamMembers")
    public List<UserDto> getUsersForTeam(UUID teamUUID) {
            return keycloakService.getUsersForTeam(teamUUID).stream().map(user -> UserDto.from(user)).collect(Collectors.toList());
    }

    public List<UserDto> getUsersForTeamByStage(UUID caseUUID, UUID stageUUID) {
        UUID teamUUID = caseworkClient.getStageTeam(caseUUID, stageUUID);
        return getUsersForTeam(teamUUID);
    }

}
