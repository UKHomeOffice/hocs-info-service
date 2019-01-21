package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.UserDto;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.value;


@Service
public class UserService {

    private KeycloakService keycloakService;
    private TeamRepository teamRepository;
    private CaseworkClient caseworkClient;

    @Autowired
    public UserService(KeycloakService keycloakService, TeamRepository teamRepository, CaseworkClient caseworkClient) {
        this.keycloakService = keycloakService;
        this.teamRepository = teamRepository;
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

    public List<UserDto> getUsersForTeam(String teamUUID) {
        Team team = teamRepository.findByUuid(UUID.fromString(teamUUID));
        if (team != null) {
            String path = String.format("/%s/%s", team.getUnit().getShortCode(), teamUUID);
            return keycloakService.getUsersForTeam(path, teamUUID).stream().map(user -> UserDto.from(user)).collect(Collectors.toList());
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Team does not exist");
        }
    }

    public List<UserDto> getUsersForTeamByStage(UUID caseUUID, UUID stageUUID) {
        UUID teamUUID = caseworkClient.getStageTeam(caseUUID, stageUUID);
        return getUsersForTeam(teamUUID.toString());
    }

}
