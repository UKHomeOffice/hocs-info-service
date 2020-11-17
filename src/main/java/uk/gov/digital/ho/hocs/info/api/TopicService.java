package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.CreateParentTopicDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateTopicDto;
import uk.gov.digital.ho.hocs.info.client.audit.client.AuditClient;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.dto.GetCaseworkCaseDataResponse;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;
import uk.gov.digital.ho.hocs.info.domain.repository.ParentTopicRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.TopicRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class TopicService {

    private final ParentTopicRepository parentTopicRepository;
    private final TopicRepository topicRepository;
    private final CaseworkClient caseworkClient;
    private final AuditClient auditClient;

    @Autowired
    public TopicService(ParentTopicRepository parentTopicRepository,
                        TopicRepository topicRepository,
                        CaseworkClient caseworkClient,
                        AuditClient auditClient) {
        this.parentTopicRepository = parentTopicRepository;
        this.topicRepository = topicRepository;
        this.caseworkClient = caseworkClient;
        this.auditClient = auditClient;
    }

    public List<ParentTopic> getParentTopics(String caseType) {
        log.debug("Requesting all parent topics for {}", caseType);
        return parentTopicRepository.findAllParentTopicByCaseType(caseType);
    }

    public List<ParentTopic> getAllParentTopics() {
        log.debug("Requesting all parent topics");
        return parentTopicRepository.findAll();
    }

    public List<Topic> getAllTopicsForParentTopic(UUID parentTopicUUID) {
        log.debug("Requesting all topics for parent topic UUID{}", parentTopicUUID);
        return topicRepository.findTopicByParentTopic(parentTopicUUID);
    }

    public Topic getTopic(UUID topicUUID) {
        log.debug("Requesting topics {}", topicUUID);
        return topicRepository.findTopicByUUID(topicUUID);
    }

    public List<ParentTopic> getTopicList(UUID caseUUID) {
        GetCaseworkCaseDataResponse caseTypeResponse = caseworkClient.getCase(caseUUID);
        return getParentTopics(caseTypeResponse.getType());
    }

    public List<Topic> getFilteredChildTopicList(UUID caseUUID) {
        GetCaseworkCaseDataResponse caseTypeResponse = caseworkClient.getCase(caseUUID);
        return findAllActiveAssignedTopicsByCaseType(caseTypeResponse.getType());
    }

    public UUID createParentTopic(CreateParentTopicDto request) {
        String displayName = request.getDisplayName();
        log.debug("Creating parent topic: {}", displayName);
        ParentTopic parentTopic = parentTopicRepository.findByDisplayName(displayName);

        if (parentTopic == null) {
            ParentTopic newParentTopic = new ParentTopic(displayName);
            parentTopicRepository.save(newParentTopic);
            log.info("Created topic: {}, with UUID: {}", displayName, newParentTopic.getUuid());
            auditClient.createParentTopicAudit(newParentTopic);
            return newParentTopic.getUuid();
        } else {
            log.debug("Unable to create parent topic, parent topic with the same name already exists");
            if (parentTopic.isActive()) {
                throw new ApplicationExceptions.TopicCreationException("Active parent topic already exists with this name");
            } else {
                throw new ApplicationExceptions.TopicCreationException("Inactive parent topic already exists with this name");
            }
        }
    }

    public UUID createTopic(CreateTopicDto request, UUID parentTopicUUID) {
        String displayName = request.getDisplayName();
        log.debug("Creating topic: {}, for parent topic: {}", displayName, parentTopicUUID);
        if (!isParentTopicValidAndActive(parentTopicUUID)) {
            log.debug("Unable to create topic, the given parent topic is invalid");
            throw new ApplicationExceptions.TopicCreationException(
                    "Unable to create topic, the given parent topic is invalid");
        } else {
            Topic existingTopic = topicRepository.findTopicByNameAndParentTopic(displayName, parentTopicUUID);
            if (existingTopic == null) {
                Topic topic = new Topic(displayName, parentTopicUUID);
                topicRepository.save(topic);
                log.info("Created topic: {}, for parent topic: {}", displayName, parentTopicUUID);
                auditClient.createTopicAudit(topic);
                return topic.getUuid();
            } else {
                if (existingTopic.isActive()) {
                    log.debug(
                            "Unable to create topic, active topic with this name already exists for this parent");
                } else {
                    log.debug(
                            "Unable to create topic, inactive topic with this name already exists for this parent");
                }
                throw new ApplicationExceptions.TopicCreationException(
                        "Topic already exists with this name for the parent topic");
            }
        }
    }


    public void deleteParentTopic(UUID parentTopicUUID) {

        log.debug("Deleting parent topic: {}", parentTopicUUID);
        ParentTopic parentTopic = parentTopicRepository.findByUuid(parentTopicUUID);
        if (parentTopic == null){
            log.debug("Unable to delete parent topic, UUID: {} does not exist", parentTopicUUID);
            throw new ApplicationExceptions.EntityNotFoundException(
                    "Unable to delete parent topic, UUID: {} does not exist", parentTopicUUID);
        } else {
            Set<Topic> childrenTopics = topicRepository.findAllActiveTopicsByParentTopic(parentTopicUUID);
            if (!childrenTopics.isEmpty()) {
                for (Topic childTopic : childrenTopics) {
                    setTopicToInactive(childTopic);
                }
            }
            parentTopic.setActive(false);
            parentTopicRepository.save(parentTopic);
            log.info("Deleted parent topic: {}", parentTopic.getDisplayName());
            auditClient.deleteParentTopicAudit(parentTopic);
        }
    }

    public void deleteTopic(UUID topicUUID) {

        log.debug("Deleting topic: {}", topicUUID);
        Topic topic = topicRepository.findTopicByUUID(topicUUID);
        if (topic == null){
            log.debug("Unable to delete  topic, UUID: {} does not exist", topicUUID);
            throw new ApplicationExceptions.EntityNotFoundException
                    ("Unable to delete topic, UUID: {} does not exist", topicUUID);
        } else {
            setTopicToInactive(topic);
        }
    }

    public void setTopicToInactive(Topic topic){
        topic.setActive(false);
        topicRepository.save(topic);
        log.info("Deleted topic: {}", topic.getDisplayName());
        auditClient.deleteTopicAudit(topic);
    }


    public void reactivateParentTopic(UUID parentTopicUUID) {

        log.debug("Reactivating parent topic: {}", parentTopicUUID);
        ParentTopic parentTopic = parentTopicRepository.findByUuid(parentTopicUUID);
        if (parentTopic == null){
            throw new ApplicationExceptions.EntityNotFoundException(
                    "Parent topic with UUID {} does not exist", parentTopicUUID.toString());
        }
        if (parentTopic.isActive()) {
            log.debug("Parent topic is already active");
        } else {
            parentTopic.setActive(true);
            parentTopicRepository.save(parentTopic);
            log.info("Activated parent topic: {}", parentTopicUUID);
            auditClient.reactivateParentTopicAudit(parentTopic);
        }
    }

    public void reactivateTopic(UUID topicUUID) {

        log.debug("Reactivating topic: {}", topicUUID);
        Topic topic = topicRepository.findTopicByUUID(topicUUID);
        if (topic == null){
            throw new ApplicationExceptions.EntityNotFoundException("Topic with UUID {} does not exist", topicUUID.toString());
        }
        ParentTopic parentTopic = parentTopicRepository.findByUuid(topic.getParentTopic());
        if (!parentTopic.isActive()) {
            log.debug("Unable to activate topic: {}, parent topic is inactive", topicUUID);
            throw new ApplicationExceptions.TopicUpdateException("Unable to activate topic, parent topic is inactive");
        } else {
            topic.setActive(true);
            topicRepository.save(topic);
            log.info("Activated topic: {}", topicUUID);
            auditClient.reactivateTopicAudit(topic);
        }
    }

    private boolean isParentTopicValidAndActive(UUID parentTopicUUID) {
        ParentTopic parentTopic = parentTopicRepository.findByUuid(parentTopicUUID);
        if (parentTopic == null) {
            log.debug("The given parent topic does not exist");
            return false;
        } else {
            if (!parentTopic.isActive()) {
                log.debug("The given parent topic is inactive");
                return false;
            } else {
                return true;
            }
        }
    }

    public List<Topic> findActiveTopicsForTeams(List<UUID> teamsUuids) {
        log.debug("Requesting all active topics for the following teams: {}", teamsUuids);
        return topicRepository.findAllActiveTopicsByTeams(teamsUuids);
    }

    public List<Topic> getTopics() {
        log.debug("Requesting all topics");
        return topicRepository.findAllBy();
    }

    private List<Topic> findAllActiveAssignedTopicsByCaseType(String caseType) {
        log.debug("Requesting all active topics for {}", caseType);
        return topicRepository.findAllActiveAssignedTopicsByCaseType(caseType);
    }
}
