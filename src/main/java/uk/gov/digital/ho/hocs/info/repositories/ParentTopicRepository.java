package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.ParentTopic;

import java.util.List;

@Repository
public interface ParentTopicRepository extends CrudRepository<ParentTopic, String> {

    @Query(value = "select * from parent_topic pt join tenant t on pt.tenant_id = t.id where t.display_name = ?1", nativeQuery = true)
    List<ParentTopic> findParentTopicByTenant(String tenant);
}
