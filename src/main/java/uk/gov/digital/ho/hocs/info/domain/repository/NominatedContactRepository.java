package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uk.gov.digital.ho.hocs.info.domain.model.NominatedContact;

import java.util.Set;
import java.util.UUID;

public interface NominatedContactRepository extends CrudRepository<NominatedContact, String> {

    @Query(value ="SELECT * FROM team_contact np WHERE team_uuid = ?1" , nativeQuery = true )
    Set<NominatedContact> findAllByTeamUUID(UUID teamUUID);
}
