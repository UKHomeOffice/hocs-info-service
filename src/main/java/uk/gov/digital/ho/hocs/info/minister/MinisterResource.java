package uk.gov.digital.ho.hocs.info.minister;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.dto.GetMembersResponse;
import uk.gov.digital.ho.hocs.info.dto.GetMinisterResponse;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.entities.Minister;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.topic.MinisterService;
import uk.gov.digital.ho.hocs.info.topic.TopicService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
public class MinisterResource {

    private final MinisterService ministerService;

    @Autowired
    public MinisterResource(final MinisterService ministerService) {
        this.ministerService = ministerService;

    }

    @RequestMapping(value = "/topic/{topicId}/minister", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetMinisterResponse> getMinisterFromTopic(@RequestHeader("X-Auth-Roles") String[] roles, @PathVariable Long topicId) {

        Minister minister = ministerService.getMinisterFromTopicId(topicId);

        return ResponseEntity.ok(GetMinisterResponse.from(minister));
    }
}