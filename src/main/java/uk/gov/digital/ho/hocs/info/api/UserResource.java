package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.UserDto;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
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
    public ResponseEntity<List<UserDto>> getUsersForTeam(@PathVariable UUID teamUUID) {
        return ResponseEntity.ok(userService.getUsersForTeam(teamUUID));
    }

    @GetMapping(value = "/teams/{teamUUID}/member/{userUUID}")
    public ResponseEntity<UserDto> getUserForTeam(@PathVariable UUID teamUUID, @PathVariable UUID userUUID) {
        return ResponseEntity.ok(userService.getUserForTeam(teamUUID, userUUID));
    }

    @GetMapping(value = "/case/{caseUUID}/stage/{stageUUID}/team/members")
    public ResponseEntity<List<UserDto>> getUsersForTeamByStage(@PathVariable UUID caseUUID, @PathVariable UUID stageUUID) {
        List<UserDto> users = userService.getUsersForTeamByStage(caseUUID, stageUUID);
        return ResponseEntity.ok(users);
    }

    @PostMapping(value = "/user", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createUser(@RequestBody CreateUserDto createUserDto) {
        userService.createUser(createUserDto);
        return ResponseEntity.ok().build();
    }


}

