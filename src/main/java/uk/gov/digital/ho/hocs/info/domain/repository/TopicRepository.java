package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;

import java.util.List;
import java.util.UUID;

@Repository
public interface TopicRepository extends CrudRepository<Topic, String> {

    @Query(value = "select * from topic t where t.parent_topic_uuid = ?1", nativeQuery = true)
    List<Topic> findTopicByParentTopic(UUID parentTopicUUID);

    @Query(value = "select * from topic t where t.uuid = ?1", nativeQuery = true)
    Topic findTopicByUUID(UUID topicUUID);
}
