package uk.gov.digital.ho.hocs.info.user;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.dto.UserDto;

@RestController
public class UserResource {

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping(value = "/teams/{teamUUID}/members")
    public ResponseEntity<List<UserDto>> getUsersForTeam(@PathVariable String teamUUID) {
        return ResponseEntity.ok(userService.getUsersForTeam(teamUUID));
    }

}

