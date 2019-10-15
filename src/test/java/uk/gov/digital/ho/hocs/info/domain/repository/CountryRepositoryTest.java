package uk.gov.digital.ho.hocs.info.domain.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.domain.model.Country;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CountryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CountryRepository repository;

    @Test()
    public void shouldFindByName() {
        Country country1 = new Country("TestCountryName1");
        this.entityManager.persist(country1);

        Country country = repository.findByName("TestCountryName1");

        assertThat(country.getName()).isEqualTo("TestCountryName1");
    }

    @Test()
    public void shouldFindAllActiveCountries() {
        Set<Country> countrys = repository.findAllActiveCountrys();

        int initialCount = countrys.size();

        Country country1 = new Country("TestCountryName1");
        this.entityManager.persist(country1);
        Country country2 = new Country("TestCountryName2");
        country2.setDeleted(true);
        this.entityManager.persist(country2);

        assertThat(repository.findAllActiveCountrys().size()).isEqualTo(initialCount + 1);
    }
}
