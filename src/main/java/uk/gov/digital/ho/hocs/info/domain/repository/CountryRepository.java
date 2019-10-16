package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Modifying;
import uk.gov.digital.ho.hocs.info.domain.model.Country;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CountryRepository extends CrudRepository<Country, String> {

    Country findByName(String name);

    @Query(value = "SELECT c.* FROM country c WHERE c.deleted = FALSE", nativeQuery = true)
    Set<Country> findAllActiveCountrys();

    @Modifying
    @Query(value = "UPDATE country SET deleted = TRUE", nativeQuery = true)
    void deleteAll();
}
