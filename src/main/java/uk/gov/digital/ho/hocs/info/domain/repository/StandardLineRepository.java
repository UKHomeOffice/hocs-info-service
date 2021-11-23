package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.StandardLine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface StandardLineRepository extends CrudRepository<StandardLine, String> {

    @Query(value = "SELECT sl.* FROM standard_line sl WHERE sl.topic_uuid = ?1 AND sl.expires > ?2", nativeQuery = true)
    StandardLine findStandardLinesByTopicAndExpires(UUID topicUUID, LocalDateTime currentDate);

    @Query(value = "SELECT sl.* FROM standard_line sl WHERE sl.topic_uuid IN ?1", nativeQuery = true)
    List<StandardLine> findStandardLinesByTopics(List<UUID> topicUUID);

    @Query(value = "SELECT sl.* FROM standard_line sl WHERE sl.expires > ?1", nativeQuery = true)
    Set<StandardLine> findStandardLinesByExpires(LocalDateTime currentDate);

    @Query(value = "SELECT sl.* FROM standard_line sl, topic t where sl.topic_uuid = t.uuid order by t.display_name, sl.display_name ", nativeQuery = true)
    List<StandardLine> findAllStandardLines();
    
    StandardLine findByUuid(UUID uuid);
}
