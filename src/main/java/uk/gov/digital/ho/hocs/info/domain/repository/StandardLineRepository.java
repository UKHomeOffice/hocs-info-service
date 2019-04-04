package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.StandardLine;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Repository
public interface StandardLineRepository extends CrudRepository<StandardLine, String> {

    @Query(value = "SELECT sl.* FROM standard_line sl WHERE sl.topic_uuid = ?1 AND sl.expires > ?2", nativeQuery = true)
    StandardLine findStandardLinesByTopicAndExpires(UUID topicUUID, LocalDateTime currentDate);

    @Query(value = "SELECT sl.* FROM standard_line sl WHERE sl.expires > ?2", nativeQuery = true)
    Set<StandardLine> findStandardLinesByExpires(LocalDateTime currentDate);
}
