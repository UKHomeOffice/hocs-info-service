package uk.gov.digital.ho.hocs.info.user;


import java.util.List;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserResource {

    private final UserService userService;


    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/users")
    public ResponseEntity<List<UserRepresentation>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }


}

