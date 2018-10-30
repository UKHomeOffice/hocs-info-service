package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.StandardLine;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface StandardLineRepository extends CrudRepository<StandardLine, String> {

    @Query(value = "SELECT sl.* FROM standard_line sl WHERE sl.topic_uuid = ?1 AND sl.expires > ?2", nativeQuery = true)
    StandardLine findStandardLinesByTopicAndExpires(UUID topicUUID, LocalDateTime currentDate);

    StandardLine findStandardLineByTopicUUID(UUID topicUUID);

    @Query(value ="SELECT sl.* FROM standard_line sl WHERE sl.display_name = ?1 AND sl.topic_uuid = ?2", nativeQuery = true )
    StandardLine findByDisplayNameAndTopic(String displayName, UUID topicUUID);
}
