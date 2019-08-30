package uk.gov.digital.ho.hocs.info.domain.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.Team;

import java.util.Set;
import java.util.UUID;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {

    Team findByUuid(UUID uuid);

    Set<Team> findAllByActiveTrue();

    @Query(value = "SELECT t.* FROM team t JOIN unit u on u.uuid = t.unit_uuid WHERE u.short_code = ?1", nativeQuery = true)
    Set<Team> findAllByActiveTrueAndUnitShortCodeEquals(String shortCode);

    Set<Team> findTeamsByUnitUuid(UUID unitUUID);

    @Query(value = "SELECT t.* FROM team t JOIN topic_team tt on tt.responsible_team_uuid = t.uuid WHERE tt.topic_uuid = ?1 and tt.stage_type = ?2", nativeQuery = true)
    Team findByTopicAndStage(UUID topicUUID, String stageType);

    @Query(value = "SELECT t.* FROM team t WHERE t.unit_uuid = ?1 and t.active = TRUE", nativeQuery = true)
    Set<Team> findActiveTeamsByUnitUuid(UUID unitUUID);
}
