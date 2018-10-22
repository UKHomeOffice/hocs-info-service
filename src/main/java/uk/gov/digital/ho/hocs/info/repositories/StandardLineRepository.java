package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.StandardLine;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface StandardLineRepository extends CrudRepository<StandardLine, String> {

    @Query(value = "SELECT sl.* FROM standard_line sl JOIN standard_line_topic slt ON sl.uuid = slt.standard_line_uuid WHERE slt.topic_uuid = ?1 AND slt.deleted = FALSE AND sl.expires > ?2", nativeQuery = true)
    List<StandardLine> findStandardLinesByTopic(UUID topicUUID, LocalDate today);

    StandardLine findStandardLineByUuid(UUID standardLineUUID);
}
