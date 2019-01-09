package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParentTopicRepository extends CrudRepository<ParentTopic, String> {

    @Query(value = "select * from parent_topic pt join parent_topic_case_type ptct on pt.uuid = ptct.parent_topic_uuid where ptct.case_type = ?1", nativeQuery = true)
    List<ParentTopic>  findAllParentTopicByCaseType(String caseType);

    @Query(value = "SELECT * from parent_topic pt where pt.responsible_team_uuid = ?1 AND pt.active = true", nativeQuery = true)
    List<ParentTopic>  findAllActiveParentTopicsForTeam(UUID teamUUID);
}
