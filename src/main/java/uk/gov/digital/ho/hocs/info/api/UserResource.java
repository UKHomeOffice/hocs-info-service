package uk.gov.digital.ho.hocs.info.api;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserResponse;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.UserDto;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
    public ResponseEntity<List<UserDto>> getUsersForTeamByStage(@PathVariable UUID caseUUID,
                                                                @PathVariable UUID stageUUID) {
        List<UserDto> users = userService.getUsersForTeamByStage(caseUUID, stageUUID);
        return ResponseEntity.ok(users);
    }

    @PostMapping(value = "/user", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        CreateUserResponse createUserResponse = userService.createUser(createUserDto);
        return ResponseEntity.ok().body(createUserResponse);
    }

    @PutMapping(value = "/user/{userUUID}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updateUser(@PathVariable UUID userUUID, @Valid @RequestBody UpdateUserDto updateUserDto) {
        userService.updateUser(userUUID, updateUserDto);
        return ResponseEntity.ok().build();
    }

}
