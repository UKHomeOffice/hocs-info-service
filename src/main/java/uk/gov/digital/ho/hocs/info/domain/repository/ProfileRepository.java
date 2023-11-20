package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.Profile;
import uk.gov.digital.ho.hocs.info.domain.model.Team;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, String> {

    @Query(value = "SELECT DISTINCT p.profile_name FROM profile p JOIN case_type_profile ctp on p.profile_name = ctp.profile_name WHERE ctp.case_type in ?1 and p.parent_system_name = ?2",
           nativeQuery = true)
    List<String> findAllProfileNamesByCaseTypesAndSystemName(Collection<String> caseTypes, String systemName);

    //@Query(value = "SELECT DISTINCT * FROM profile p JOIN case_type_profile ctp on p.profile_name = ctp.profile_name WHERE ctp.case_type = ?1 and p.parent_system_name = ?2",
    @Query(value = "SELECT DISTINCT p.profile_name, p.parent_system_name, p.summary_deadlines_enabled, ctp.case_type FROM profile p JOIN case_type_profile ctp on p.profile_name = ctp.profile_name WHERE ctp.case_type = ?1 and p.parent_system_name = ?2",
           nativeQuery = true)
    Profile findByCaseTypeAndSystemName(String caseType, String systemName);

}
