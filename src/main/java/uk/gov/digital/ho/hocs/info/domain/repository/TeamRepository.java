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

    @Query(value = "SELECT t.* FROM team t JOIN team_link tl on tl.responsible_team_uuid = t.uuid WHERE tl.link_uuid = ?1 and tl.link_type = 'TOPIC' and tl.stage_type = ?2", nativeQuery = true)
    Team findByTopicAndStage(UUID topicUUID, String stageType);

    @Query(value = "SELECT t.* FROM team t JOIN team_link tl on tl.responsible_team_uuid = t.uuid WHERE tl.link_uuid = ?1 and tl.link_type = 'REGION' and tl.stage_type = ?2", nativeQuery = true)
    Team findByRegionAndStage(UUID regionUUID, String stageType);

    @Query(value = "SELECT t.* FROM team t WHERE t.unit_uuid = ?1 and t.active = TRUE", nativeQuery = true)
    Set<Team> findActiveTeamsByUnitUuid(UUID unitUUID);
}
