package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.HouseAddress;

@Repository
public interface HouseAddressRepository extends CrudRepository<HouseAddress, String> {

    HouseAddress findByHouseCode(String houseCode);
}
