package uk.gov.digital.ho.hocs.info.api.topic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.api.dto.GetAllTopicsResponse;
import uk.gov.digital.ho.hocs.info.api.dto.GetParentTopicsResponse;
import uk.gov.digital.ho.hocs.info.api.dto.GetTopicsResponse;
import uk.gov.digital.ho.hocs.info.api.dto.TopicDto;
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
public class TopicResource {

    private final TopicService topicService;

    @Autowired
    public TopicResource(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping(value = "/topics/{caseType}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetAllTopicsResponse> getAllTopicsByCaseType(@PathVariable String caseType) {
            log.info("requesting all Parent topics and topics for case type {}", caseType);
            List<ParentTopic> parentTopics = topicService.getParentTopics(caseType);
            return ResponseEntity.ok(GetAllTopicsResponse.from(parentTopics));
    }

    @GetMapping(value = "/case/{caseUUID}/topiclist", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetAllTopicsResponse> getParentTopicsAndTopics(@PathVariable UUID caseUUID) {
        List<ParentTopic> parentTopics = topicService.getTopicList(caseUUID);
        return ResponseEntity.ok(GetAllTopicsResponse.from(parentTopics));
    }

    @GetMapping(value = "/topic/parent/{caseType}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetParentTopicsResponse> getAllParentTopicsByCaseType(@PathVariable String caseType) {
        log.info("requesting all Parent topics for case type {}", caseType);
        List<ParentTopic> parentTopics = topicService.getParentTopics(caseType);
        return ResponseEntity.ok(GetParentTopicsResponse.from(parentTopics));
    }

    @GetMapping(value = "/topic/all/{parentTopicUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetTopicsResponse> getTopicsByParentTopic(@PathVariable UUID parentTopicUUID) {
            log.info("requesting all topics for parent topic uuid {}", parentTopicUUID);
            List<Topic> topics = topicService.getAllTopicsForParentTopic(parentTopicUUID);
            return ResponseEntity.ok(GetTopicsResponse.from(topics));
    }

    @GetMapping(value = "/topic/{topicUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<TopicDto> getTopicByUUID(@PathVariable UUID topicUUID) {
        log.info("requesting topic {}", topicUUID);
        Topic topics = topicService.getTopic(topicUUID);
        return ResponseEntity.ok(TopicDto.from(topics));
    }
}
