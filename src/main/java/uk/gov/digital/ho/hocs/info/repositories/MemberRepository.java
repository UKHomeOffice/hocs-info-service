package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.Member;

import java.util.Set;
import java.util.UUID;

@Repository
public interface MemberRepository extends CrudRepository<Member, String> {

    Member findByExternalReference(String ExtRef);

    @Query(value = "SELECT m.* FROM member m WHERE m.deleted = FALSE", nativeQuery = true)
    Set<Member> findAllActiveMembers();

    @Query(value="SELECT m.*, ha.address1 as address1, ha.address2 as address2, ha.address3 as address3, ha.postcode as postcode, ha.country as country, FROM Members m JOIN house_address ha ON m.house = ha.house WHERE m.uuid = ?1", nativeQuery = true)
    Member findMemberAndAddressByUUID(UUID uuid);

}
