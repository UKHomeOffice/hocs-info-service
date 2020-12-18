package uk.gov.digital.ho.hocs.info.domain.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.api.data.SimpleMapItem;
import uk.gov.digital.ho.hocs.info.domain.model.Team;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {

    Team findByUuid(UUID uuid);

    Set<Team> findAll();

    Set<Team> findAllByActiveTrue();

    @Query(value = "SELECT t.* FROM team t JOIN unit u on u.uuid = t.unit_uuid WHERE u.short_code = ?1", nativeQuery = true)
    Set<Team> findAllByActiveTrueAndUnitShortCodeEquals(String shortCode);

    Set<Team> findTeamsByUnitUuid(UUID unitUUID);

    @Query(value = "SELECT t.* FROM team t JOIN team_link tl on tl.responsible_team_uuid = t.uuid WHERE tl.link_value = cast(?1 as text) and tl.link_type = 'TOPIC' and tl.stage_type = ?2", nativeQuery = true)
    Team findByTopicAndStage(UUID topicUUID, String stageType);

    @Query(value = "SELECT DISTINCT tl.link_value as value, t.display_name as label FROM team t JOIN team_link tl on tl.responsible_team_uuid = t.uuid WHERE tl.link_type = 'TOPIC' and tl.stage_type = ?1", nativeQuery = true)
    List<SimpleMapItem> findTopicToTeamMappingByStageType(String stageType);

    @Query(value = "SELECT t.* FROM team t JOIN team_link tl on tl.responsible_team_uuid = t.uuid WHERE tl.stage_type = ?1 and tl.link_type = 'TEXT' and tl.link_value = ?2", nativeQuery = true)
    Team findByStageAndText(String stageType, String text);

    @Query(value = "SELECT t.* FROM team t WHERE t.unit_uuid = ?1 and t.active = TRUE", nativeQuery = true)
    Set<Team> findActiveTeamsByUnitUuid(UUID unitUUID);

    @Query(value = "SELECT t.* FROM team t INNER JOIN team_link tl ON tl.responsible_team_uuid=t.uuid INNER JOIN topic ON cast(topic.uuid as text)=tl.link_value WHERE tl.link_type='TOPIC' AND topic.uuid=?1 AND topic.active=TRUE AND t.active=TRUE", nativeQuery = true)
    Set<Team> findTeamsByTopicUuid(UUID topicUUID);
}
