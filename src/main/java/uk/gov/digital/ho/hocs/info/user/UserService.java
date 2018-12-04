package uk.gov.digital.ho.hocs.info.user;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.dto.UserDto;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.TeamRepository;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UserService {

    private KeycloakService keycloakService;
    private TeamRepository teamRepository;

    @Autowired
    public UserService(KeycloakService keycloakService, TeamRepository teamRepository) {
        this.keycloakService = keycloakService;
        this.teamRepository = teamRepository;
    }

    @Cacheable("users")
    public List<UserDto> getAllUsers() {
        return keycloakService.getAllUsers().stream().map(user -> UserDto.from(user)).collect(Collectors.toList());
    }

    @CachePut("users")
    public List<UserDto> refreshUserCache() {
        return keycloakService.getAllUsers().stream().map(user -> UserDto.from(user)).collect(Collectors.toList());
    }

    public List<UserDto> getUsersForTeam(String teamUUID) {
        Team team = teamRepository.findByUuid(UUID.fromString(teamUUID));
        if (team != null) {
            String path = String.format("/%s/%s", team.getUnit().getShortCode(), teamUUID);
            return keycloakService.getUsersForTeam(path, teamUUID).stream().map(user -> UserDto.from(user)).collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException("Team does not exist");
        }

    }
}
