package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.Member;

import java.util.Set;

@Repository
public interface MemberRepository extends CrudRepository<Member, String> {

    Member findByExternalReference(String ExtRef);

    @Query(value = "SELECT m.* FROM member m WHERE m.deleted = FALSE", nativeQuery = true)
    Set<Member> findAllActiveMembers();

}
