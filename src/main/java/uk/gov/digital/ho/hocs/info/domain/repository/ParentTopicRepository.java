package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParentTopicRepository extends CrudRepository<ParentTopic, String> {

    @Query(value = "SELECT DISTINCT pt.* from topic t join topic_team tt on t.uuid = tt.topic_uuid join parent_topic pt on t.parent_topic_uuid = pt.uuid where tt.case_type = ?1 and pt.active = true", nativeQuery = true)
    List<ParentTopic>  findAllParentTopicByCaseType(String caseType);

    @Query(value = "SELECT DISTINCT pt.* from topic t join topic_team tt on t.uuid = tt.topic_uuid join parent_topic pt on t.parent_topic_uuid = pt.uuid where tt.responsible_team_uuid = ?1 and pt.active = true", nativeQuery = true)
    List<ParentTopic>  findAllActiveParentTopicsForTeam(UUID teamUUID);

    ParentTopic findByUuid(UUID uuid);

    ParentTopic findByDisplayName(String displayName);


}
