package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.entities.Minister;

import java.util.List;

@Repository
public interface MinisterRepository extends CrudRepository<Minister, String> {

    @Query(value ="SELECT m.* FROM minister m JOIN topic t ON t.minister_id = m.id WHERE t.id = ?1", nativeQuery = true)
    Minister findByTopicId(Long topicId);
}
