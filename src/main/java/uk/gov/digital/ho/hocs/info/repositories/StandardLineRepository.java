package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.StandardLine;

import java.util.List;
import java.util.UUID;

@Repository
public interface StandardLineRepository extends CrudRepository<StandardLine, String> {

    @Query(value = "SELECT sl.* FROM standard_line sl JOIN standard_line_topic slt ON sl.uuid = slt.standard_line_uuid WHERE slt.topic_uuid = ?1 AND slt.active = TRUE", nativeQuery = true)
    List<StandardLine> findStandardLinesByCaseTopic(UUID topicUUID);

    StandardLine findStandardLineByUuid(UUID standardLineUUID);
}
