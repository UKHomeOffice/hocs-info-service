package uk.gov.digital.ho.hocs.info.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.entities.ParentTopic;
import uk.gov.digital.ho.hocs.info.repositories.ParentTopicRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicService {


    private final ParentTopicRepository parentTopicRepository;

    private final RequestData requestData;

    @Autowired
    public TopicService(ParentTopicRepository parentTopicRepository, RequestData requestData) {
        this.parentTopicRepository = parentTopicRepository;
        this.requestData = requestData;
    }

    public List<ParentTopic> getTopics(List<String> roles) {
       // throw new NotImplementedException();
        return new ArrayList<>();
    }
}
