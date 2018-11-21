package uk.gov.digital.ho.hocs.info.topic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.caseworkclient.dto.GetCaseworkCaseDataResponse;
import uk.gov.digital.ho.hocs.info.entities.ParentTopic;
import uk.gov.digital.ho.hocs.info.entities.Topic;
import uk.gov.digital.ho.hocs.info.repositories.ParentTopicRepository;
import uk.gov.digital.ho.hocs.info.repositories.TopicRepository;

import java.util.List;
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
            return  parentTopicRepository.findAllParentTopicByCaseType(caseType);
    }

    public List<Topic> getAllTopicsForParentTopic(UUID parentTopicUUID) {
        log.debug("Requesting all topics for parent topic UUID{}", parentTopicUUID);
        return  topicRepository.findTopicByParentTopic(parentTopicUUID);
    }

    public Topic getTopic(UUID topicUUID) {
        log.debug("Requesting topics {}", topicUUID);
        return  topicRepository.findTopicByUUID(topicUUID);
    }

    public List<ParentTopic> getTopicList(UUID caseUUID) {
        GetCaseworkCaseDataResponse caseTypeResponse = caseworkClient.getCase(caseUUID);
        return getParentTopics(caseTypeResponse.getType());
    }
}
