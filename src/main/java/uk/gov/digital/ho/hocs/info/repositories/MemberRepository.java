package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.Member;

import java.util.Set;

@Repository
public interface MemberRepository extends CrudRepository<Member, String> {

//    @Query(value = "SELECT m.* FROM member m JOIN house_case_type lt ON m.house = lt.house WHERE lt.case_type = ?1 AND m.active = TRUE", nativeQuery = true)
//    Set<Member> findAllActiveMembersForCaseType(String caseType);

    @Query(value = "SELECT m.* FROM member m JOIN member_case_type mct ON m.uuid = mct.member_uuid WHERE mct.case_type = ?1 AND m.deleted = FALSE ", nativeQuery = true)
    Set<Member> findAllActiveMembersForCaseType(String caseType);

    @Query(value = "SELECT m.* FROM member m WHERE m.deleted = FALSE", nativeQuery = true)
    Set<Member> findAllActiveMembers();
}
