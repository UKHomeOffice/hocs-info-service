package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.Constituency;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConstituencyRepository extends CrudRepository<Constituency, String> {

    @Query(value = "SELECT DISTINCT c.* from constituency c join team_link tl on c.uuid = tl.link_uuid where tl.case_type = ?1 and c.active = true", nativeQuery = true)
    List<Constituency>  findAllActiveConstituencyByCaseType(String caseType);

    @Query(value = "SELECT DISTINCT c.* from constituency c join team_link tl on t.uuid = tl.link_uuid where tl.responsible_team_uuid = ?1 and c.active = true", nativeQuery = true)
    List<Constituency>  findAllActiveConstituencysForTeam(UUID teamUUID);

    @Query(value = "select * from constituency c where c.uuid = ?1", nativeQuery = true)
    Constituency findConstituencyByUUID(UUID constituencyUUID);

    @Query(value = "select * from constituency c join member m on c.uuid = m.constituency_uuid where m.external_reference = ?1", nativeQuery = true)
    Constituency findConstituencyByMemberExternalReference(String externalReference);

    @Query(value = "SELECT * FROM constituency c WHERE c.constituency_name = ?1 and c.active = true", nativeQuery = true)
    Constituency findActiveConstituencyByName(String constituencyName);

    @Query(value = "SELECT * FROM constituency c WHERE c.constituency_name = ?1", nativeQuery = true)
    Constituency findConstituencyByName(String constituencyName);

    List<Constituency> findAllByActiveIsTrue();

    List<Constituency> findAllBy();
}
