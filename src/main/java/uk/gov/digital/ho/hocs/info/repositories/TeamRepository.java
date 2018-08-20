package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.Team;

@Repository
public interface TeamRepository extends CrudRepository<Team, String> {

    @Query(value = "SELECT t.* FROM team t JOIN member m on m.team_id = t.id WHERE m.id = ?1", nativeQuery = true)
    Team getTeamFromMemberId(int topicId);

    @Query(value = "SELECT t.* FROM team t JOIN topic p on p.team_id = t.id WHERE p.id = ?1", nativeQuery = true)
    Team getTeamFromTopicId(int topicId);
}
