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
    List<Topic> findTopicByParentTopic(UUID parentTopicUuid);

    @Query(value = "select * from topic t where t.uuid = ?1", nativeQuery = true)
    Topic findTopicByUUID(UUID topicUUID);

    @Query(value = "SELECT * FROM topic t WHERE t.display_name = ?1 AND t.parent_topic_uuid = ?2", nativeQuery = true)
    Topic findTopicByNameAndParentTopic(String displayName, UUID parentTopicUuid);

    @Query(value = "SELECT * FROM topic t WHERE t.active = true AND t.parent_topic_uuid = ?1", nativeQuery = true)
    Set<Topic> findAllActiveTopicsByParentTopic(UUID parentTopicUuid);

    List<Topic> findAllByActiveIsTrue();

    List<Topic> findAllBy();

    @Query(value = "SELECT DISTINCT t.* FROM topic t JOIN team_link tl ON cast(t.uuid as text) = tl.link_value JOIN parent_topic pt ON t.parent_topic_uuid = pt.uuid WHERE tl.case_type = ?1 AND pt.active = TRUE AND t.active = TRUE", nativeQuery = true)
    List<Topic>  findTopicsByCaseType(String caseType);

    @Query(value = "SELECT DISTINCT t.* from info.topic t join info.team_link tl on cast(t.uuid as text) = tl.link_value join info.parent_topic pt on t.parent_topic_uuid = pt.uuid where tl.case_type = ?1 and pt.active = true and t.active = true order by t.display_name", nativeQuery = true)
    List<Topic>  findAllActiveAssignedTopicsByCaseType(String caseType);

    @Query(value = "SELECT DISTINCT t.* FROM info.team_link tl INNER JOIN info.topic t ON cast(t.uuid as text) = tl.link_value WHERE tl.responsible_team_uuid in ?1 AND t.active = TRUE", nativeQuery = true)
    List<Topic> findAllActiveTopicsByTeams(List<UUID> teamsUuids);
}
