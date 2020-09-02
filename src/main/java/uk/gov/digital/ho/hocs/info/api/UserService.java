package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.UserDto;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserService {

    private KeycloakService keycloakService;
    private CaseworkClient caseworkClient;
    private StageTypeService stageTypeService;

    @Autowired
    public UserService(KeycloakService keycloakService, CaseworkClient caseworkClient, StageTypeService stageTypeService) {
        this.keycloakService = keycloakService;
        this.caseworkClient = caseworkClient;
        this.stageTypeService = stageTypeService;
    }

    @Cacheable("users")
    public List<UserDto> getAllUsers() {
        log.info("Retrieving all users from Keycloak");
        List<UserDto> users = keycloakService.getAllUsers().stream().map(user -> UserDto.from(user)).collect(Collectors.toList());
        log.info("Found {} users", users.size());
        return users;
    }

    @CachePut("users")
    public List<UserDto> refreshUserCache() {
        log.info("Refreshing User cache");
        return keycloakService.getAllUsers().stream().map(user -> UserDto.from(user)).collect(Collectors.toList());
    }

    public void createUser(CreateUserDto createUserDto) {
        keycloakService.createUser(createUserDto);
    }

    public UserDto getUserByUUID(UUID userUUID) {
        return UserDto.from(keycloakService.getUserFromUUID(userUUID));
    }

    @Cacheable(value = "teamMembers")
    public List<UserDto> getUsersForTeam(UUID teamUUID) {
        return keycloakService.getUsersForTeam(teamUUID).stream().map(user -> UserDto.from(user)).collect(Collectors.toList());
    }

    public List<UserDto> getUsersForTeamByStage(UUID caseUUID, UUID stageUUID) {
        String stageType = caseworkClient.getStageTypeFromStage(caseUUID, stageUUID);
        UUID teamUUID = stageTypeService.getTeamForStageType(stageType).getUuid();
        return getUsersForTeam(teamUUID);
    }


}
