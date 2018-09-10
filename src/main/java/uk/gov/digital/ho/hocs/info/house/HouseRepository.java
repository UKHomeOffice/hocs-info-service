package uk.gov.digital.ho.hocs.info.house;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.House;

import java.util.Set;

@Repository
public interface HouseRepository extends CrudRepository<House, Long> {

    House findOneByName(String name);
}