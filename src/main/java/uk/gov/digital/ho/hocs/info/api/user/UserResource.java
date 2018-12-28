package uk.gov.digital.ho.hocs.info.api.user;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.UserDto;

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

    @GetMapping(value = "/user/{userUUID}")
    public ResponseEntity<UserDto> getUserByUUID(@PathVariable UUID userUUID) {
        return ResponseEntity.ok(userService.getUserByUUID(userUUID));
    }

    @GetMapping(value = "/teams/{teamUUID}/members")
    public ResponseEntity<List<UserDto>> getUsersForTeam(@PathVariable String teamUUID) {
        return ResponseEntity.ok(userService.getUsersForTeam(teamUUID));
    }

}

