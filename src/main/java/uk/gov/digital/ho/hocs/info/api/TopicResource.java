package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.*;
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;
import uk.gov.digital.ho.hocs.info.domain.repository.ParentTopicRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
public class TopicResource {

    private final TopicService topicService;
    private final ParentTopicRepository parentTopicRepository;

    @Autowired
    public TopicResource(TopicService topicService, ParentTopicRepository parentTopicRepository) {
        this.topicService = topicService;
        this.parentTopicRepository = parentTopicRepository;
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
        List<Topic> filteredChildTopics = topicService.getFilteredChildTopicList(caseUUID);
        return ResponseEntity.ok(GetAllTopicsResponse.fromTopicWithFilteredChildren(parentTopics, filteredChildTopics));
    }

    // todo: remove
    @GetMapping(value = "/case/foitopiclist", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<TopicDto>> getAllFOITopics() {
        log.info("requesting all topics from FOI Topics list");
        ParentTopic parentTopic = parentTopicRepository.findByDisplayName("FOI Topics");
        List<Topic> topics = topicService.getAllTopicsForParentTopic(parentTopic.getUuid());
        return ResponseEntity.ok(topics.stream().map(t->TopicDto.from(t)).collect(Collectors.toSet()));
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

    @GetMapping(value = "/topics", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<TopicDto>> getTopics () {
        log.info("requesting all topics");
        List<Topic> topics = topicService.getTopics();
        return ResponseEntity.ok(topics.stream().map(t->TopicDto.from(t)).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/topics/active", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<TopicDto>> getActiveTopics () {
        log.info("requesting all active topics");
        List<Topic> topics = topicService.getActiveTopics();
        return ResponseEntity.ok(topics.stream().map(t->TopicDto.from(t)).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/topic/parents", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<ParentTopicDto>> getParentTopics () {
        log.info("requesting all parent topics");
        List<ParentTopic> parentTopics = topicService.getAllParentTopics();
        return ResponseEntity.ok(parentTopics.stream().map(t->ParentTopicDto.from(t)).collect(Collectors.toSet()));
    }

    @PostMapping(value = "/topic/parent", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CreateParentTopicResponse> createParentTopic(@RequestBody CreateParentTopicDto request) {
        log.info("Creating new parent topic {}", request.getDisplayName());
        UUID parentTopicUUID = topicService.createParentTopic(request);
        return ResponseEntity.ok(new CreateParentTopicResponse(parentTopicUUID.toString()));
    }

    @PostMapping(value = "/topic/parent/{parentTopicUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CreateTopicResponse> createTopic(@RequestBody CreateTopicDto request, @PathVariable UUID parentTopicUUID) {
        log.info("Creating new topic {}", request.getDisplayName());
        UUID topicUUID = topicService.createTopic(request, parentTopicUUID);
        return ResponseEntity.ok(new CreateTopicResponse(topicUUID.toString()));
    }

    @DeleteMapping(value = "/topic/parent/{parentTopicUUID}")
    public ResponseEntity deleteParentTopic(@PathVariable UUID parentTopicUUID) {
        topicService.deleteParentTopic(parentTopicUUID);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/topic/{topicUUID}")
    public ResponseEntity deleteTopic(@PathVariable UUID topicUUID) {
        topicService.deleteTopic(topicUUID);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/topic/parent/{parentTopicUUID}")
    public ResponseEntity reactivateParentTopic(@PathVariable UUID parentTopicUUID){
        topicService.reactivateParentTopic(parentTopicUUID);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/topic/{topicUUID}")
    public ResponseEntity reactivateTopic(@PathVariable UUID topicUUID){
        topicService.reactivateTopic(topicUUID);
        return ResponseEntity.ok().build();
    }
}
