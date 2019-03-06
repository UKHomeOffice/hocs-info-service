package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.CreateParentTopicDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateTopicDto;
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

    @Autowired
    public TopicService(ParentTopicRepository parentTopicRepository, TopicRepository topicRepository, CaseworkClient caseworkClient) {
        this.parentTopicRepository = parentTopicRepository;
        this.topicRepository = topicRepository;
        this.caseworkClient = caseworkClient;
    }

    public List<ParentTopic> getParentTopics(String caseType) {
        log.debug("Requesting all parent topics for {}", caseType);
        return parentTopicRepository.findAllParentTopicByCaseType(caseType);
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

    public UUID createParentTopic(CreateParentTopicDto request) {
        String displayName = request.getDisplayName();
        new ParentTopic(displayName);
        log.debug("Creating parent topic: {}", displayName);
        ParentTopic parentTopic = parentTopicRepository.findByDisplayName(displayName);

        if (parentTopic == null) {
            ParentTopic newParentTopic = new ParentTopic(displayName);
            parentTopicRepository.save(newParentTopic);
            log.info("Created topic: {}, with UUID: {}", displayName, newParentTopic.getUuid());
            return newParentTopic.getUuid();
        } else {
            if (parentTopic.getActive() == true) {
                log.debug("Unable to create parent topic, active parent topic with the same name already exists");
            } else {
                log.debug("Unable to create parent topic, inactive parent topic with the same name already exists");
            }
            throw new ApplicationExceptions.TopicCreationException("Parent topic already exists with this name");
        }
    }

    public UUID createTopic(CreateTopicDto request, UUID parentTopicUUID) {
        String displayName = request.getDisplayName();
        log.debug("Creating topic: {}, for parent topic: {}", displayName, parentTopicUUID);
        ParentTopic parentTopic = parentTopicRepository.findByUuid(parentTopicUUID);
        if (parentTopic == null) {
            throw new ApplicationExceptions.TopicCreationException(
                    "Unable to create topic, the given parent topic does not exist");
        } else {
            if (parentTopic.getActive() == false) {
                throw new ApplicationExceptions.TopicCreationException(
                        "Unable to create topic, the given parent topic is inactive");
            } else {
                Topic existingTopic = topicRepository.findTopicByNameAndParentTopic(displayName, parentTopicUUID);
                if (existingTopic == null) {
                    Topic topic = new Topic(displayName, parentTopicUUID);
                    topicRepository.save(topic);
                    log.info("Created topic: {}, for parent topic: {}", displayName, parentTopicUUID);
                    return topic.getUuid();
                } else {
                    if (existingTopic.getActive() == true) {
                        log.debug(
                                "Unable to create topic, active topic with this name already exists for this parent");
                    } else {
                        log.debug(
                                "Unable to create topic, inactive topic with this name already exists for this parent");
                    }
                    throw new ApplicationExceptions.TopicCreationException(
                            "Topic already exists with this name for the given parent");
                }
            }
        }
    }

    public void deleteParentTopic(UUID parentTopicUUID) {

        log.debug("Deleting parent topic: {}", parentTopicUUID);

        ParentTopic parentTopic = parentTopicRepository.findByUuid(parentTopicUUID);
        if (parentTopic == null){
            log.debug("Unable to delete parent topic, UUID: {} does not exist", parentTopicUUID);
            throw new ApplicationExceptions.EntityNotFoundException("Unable to delete parent topic, UUID: {} does not exist", parentTopicUUID);
        } else {
            Set<Topic> childrenTopics = topicRepository.findAllActiveTopicsByParentTopic(parentTopicUUID);
            if (!childrenTopics.isEmpty()) {
                for (Topic childTopic : childrenTopics) {
                    childTopic.setActive(false);
                    topicRepository.save(childTopic);
                    log.info("Deleted child topic: {}", childTopic.getDisplayName());
                }
            }
            parentTopic.setActive(false);
            parentTopicRepository.save(parentTopic);
            log.info("Deleted parent topic: {}", parentTopic.getDisplayName());

        }
    }

    public void deleteTopic(UUID topicUUID) {

        log.debug("Deleting topic: {}", topicUUID);

        Topic topic = topicRepository.findTopicByUUID(topicUUID);
        if (topic == null){
            log.debug("Unable to delete  topic, UUID: {} does not exist", topicUUID);
            throw new ApplicationExceptions.EntityNotFoundException("Unable to delete topic, UUID: {} does not exist", topicUUID);
        } else {
            topic.setActive(false);
            topicRepository.save(topic);
            log.info("Deleted topic: {}", topic.getDisplayName());
        }
    }
}

