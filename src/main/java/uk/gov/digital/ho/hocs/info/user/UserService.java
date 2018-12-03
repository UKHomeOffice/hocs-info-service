package uk.gov.digital.ho.hocs.info.user;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.TeamRepository;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.util.List;
import java.util.UUID;


@Service
public class UserService {

    private KeycloakService keycloakService;
    private TeamRepository teamRepository;

    @Autowired
    public UserService(KeycloakService keycloakService, TeamRepository teamRepository) {
        this.keycloakService = keycloakService;
        this.teamRepository = teamRepository;
    }

    public List<UserRepresentation> getAllUsers() {
        return keycloakService.getAllUsers();
    }

    public List<UserRepresentation> getUsersForTeam(String teamUUID) {
        Team team = teamRepository.findByUuid(UUID.fromString(teamUUID));
        if (team != null) {
            String path = String.format("/%s/%s", team.getUnit().getShortCode(), teamUUID);
            return keycloakService.getUsersForTeam(path, teamUUID);
        } else {
            throw new EntityNotFoundException("Team does not exist");
        }

    }
}
