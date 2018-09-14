package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.ParentTopic;

import java.util.List;

@Repository
public interface ParentTopicRepository extends CrudRepository<ParentTopic, String> {

    @Query(value = "select * from parent_topic pt join parent_topic_case_type ptct on pt.uuid = ptct.parent_topic_uuid where ptct.case_type = ?1", nativeQuery = true)
    List<ParentTopic>  findAllParentTopicByCaseType(String caseType);

}
