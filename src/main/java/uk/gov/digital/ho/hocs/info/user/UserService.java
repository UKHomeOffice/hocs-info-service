package uk.gov.digital.ho.hocs.info.user;

import java.util.List;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;


@Service
public class UserService {

    private KeycloakService keycloakService;

    @Autowired
    public UserService(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    public List<UserRepresentation> getAllUsers() {
        return keycloakService.getAllUsers();
    }

}
