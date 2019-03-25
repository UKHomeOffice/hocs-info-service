package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TopicRepository extends CrudRepository<Topic, String> {

    @Query(value = "select * from topic t where t.parent_topic_uuid = ?1", nativeQuery = true)
    List<Topic> findTopicByParentTopic(UUID parent_topic_uuid);

    @Query(value = "select * from topic t where t.uuid = ?1", nativeQuery = true)
    Topic findTopicByUUID(UUID topicUUID);

    @Query(value = "SELECT * FROM topic t WHERE t.display_name = ?1 AND t.parent_topic_uuid = ?2", nativeQuery = true)
    Topic findTopicByNameAndParentTopic(String displayName, UUID parent_topic_uuid);

    @Query(value = "SELECT * FROM topic t WHERE t.active = true AND t.parent_topic_uuid = ?1", nativeQuery = true)
    Set<Topic> findAllActiveTopicsByParentTopic(UUID parent_topic_uuid);

    List<Topic> findAllByActiveIsTrue();

    Set<Topic> findAllBy();
}
