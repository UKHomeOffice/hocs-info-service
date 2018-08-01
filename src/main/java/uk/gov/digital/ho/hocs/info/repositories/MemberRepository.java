package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.entities.Team;

import java.util.Set;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {

    Set<Member> findAll();

    Set<Member> findAllBy();
}
