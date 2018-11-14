package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uk.gov.digital.ho.hocs.info.entities.NominatedPerson;

import java.util.Set;
import java.util.UUID;

public interface NominatedPersonRepository extends CrudRepository<NominatedPerson, String> {

    @Query(value ="SELECT * FROM nominated_person np WHERE team_uuid = ?1" , nativeQuery = true )
    Set<NominatedPerson> findAllByTeamUUID(UUID teamUUID);
}
