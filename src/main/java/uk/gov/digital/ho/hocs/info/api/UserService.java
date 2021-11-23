package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserResponse;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateUserDto;
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

    public List<UserDto> getAllUsers() {
        log.info("Retrieving all users from Keycloak");
        List<UserDto> users = keycloakService.getAllUsers().stream().map(user -> UserDto.from(user)).collect(Collectors.toList());
        log.info("Found {} users", users.size());
        return users;
    }

    public CreateUserResponse createUser(CreateUserDto createUserDto) {
        return keycloakService.createUser(createUserDto);
    }

    public void updateUser(UUID userUUID, UpdateUserDto updateUserDto) {
        keycloakService.updateUser(userUUID, updateUserDto);
    }

    public UserDto getUserByUUID(UUID userUUID) {
        return UserDto.from(keycloakService.getUserFromUUID(userUUID));
    }

    public List<UserDto> getUsersForTeam(UUID teamUUID) {
        return keycloakService.getUsersForTeam(teamUUID).stream().map(user -> UserDto.from(user)).collect(Collectors.toList());
    }

    public UserDto getUserForTeam(UUID teamUUID, UUID userUUID) {
        String userId = userUUID.toString();
        List<UserDto> users = getUsersForTeam(teamUUID);
        for (UserDto user : users) {
            if (user.getId().equals(userId)){
                return user;
            }
        }
        return null;
    }

    public List<UserDto> getUsersForTeamByStage(UUID caseUUID, UUID stageUUID) {
        String stageType = caseworkClient.getStageTypeFromStage(caseUUID, stageUUID);
        UUID teamUUID = stageTypeService.getTeamForStageType(stageType).getUuid();
        return getUsersForTeam(teamUUID);
    }


}
