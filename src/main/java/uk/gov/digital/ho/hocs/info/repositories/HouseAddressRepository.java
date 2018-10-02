package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.HouseAddress;

@Repository
public interface HouseAddressRepository extends CrudRepository<HouseAddress, String> {

    HouseAddress findByHouseCode(String houseCode);
}
