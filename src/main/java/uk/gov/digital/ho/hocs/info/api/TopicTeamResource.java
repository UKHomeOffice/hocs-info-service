package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.*;

import java.util.UUID;

@Slf4j
@RestController
public class TopicTeamResource {

    private final TopicTeamService topicTeamService;

    @Autowired
    public TopicTeamResource(TopicTeamService topicTeamService) {
        this.topicTeamService = topicTeamService;
    }

    @PostMapping(value = "/topic/{topicUUID}/team/{teamUUID}")
    public ResponseEntity addTeamToTopic(@PathVariable UUID topicUUID, @PathVariable UUID teamUUID, @RequestBody AddTeamToTopicDto request) {
        log.info("Adding team () to topic {}", teamUUID, topicUUID);
        topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
        return ResponseEntity.ok().build();
    }

}