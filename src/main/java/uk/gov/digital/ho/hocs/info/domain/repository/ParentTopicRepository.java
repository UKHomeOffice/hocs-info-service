package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ParentTopicRepository extends CrudRepository<ParentTopic, String> {

    @Query(value = "SELECT DISTINCT pt.* from topic t join team_link tl on cast(t.uuid as text) = tl.link_value join parent_topic pt on t.parent_topic_uuid = pt.uuid where tl.case_type = ?1 and pt.active = true",
           nativeQuery = true)
    List<ParentTopic> findAllParentTopicByCaseType(String caseType);

    @Query(value = "SELECT DISTINCT pt.* from topic t join team_link tl on cast(t.uuid as text) = tl.link_value join parent_topic pt on t.parent_topic_uuid = pt.uuid where tl.responsible_team_uuid = ?1 and pt.active = true",
           nativeQuery = true)
    List<ParentTopic> findAllActiveParentTopicsForTeam(UUID teamUUID);

    ParentTopic findByUuid(UUID uuid);

    ParentTopic findByDisplayName(String displayName);

    Set<ParentTopic> findAllByActiveIsTrue();

    List<ParentTopic> findAll();

}
