package uk.gov.digital.ho.hocs.info.domain.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.domain.model.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ConstituencyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ConstituencyRepository repository;

    UUID constituencyUUID;

    @Before
    public void setup() {
        constituencyUUID = UUID.randomUUID();
        Constituency constituency = new Constituency(1L, constituencyUUID, "constituency",true);
        entityManager.merge(constituency);
    }

    @Test()
    public void shouldFindAll() {
        List<Constituency> constituencys = new ArrayList<Constituency>((Collection<? extends Constituency>) repository.findAll());
        assertThat(constituencys.get(0).getUuid()).isEqualTo(constituencyUUID);
        assertThat(constituencys.size()).isEqualTo(1);
    }

    @Test()
    public void shouldFindAllActiveIsTrue() {
        List<Constituency> constituencys = new ArrayList<Constituency>((Collection<? extends Constituency>) repository.findAllByActiveIsTrue());
        assertThat(constituencys.get(0).getUuid()).isEqualTo(constituencyUUID);
        assertThat(constituencys.size()).isEqualTo(1);
    }

    @Test()
    public void shouldFindConstituencyByNameWhenExists() {
        Constituency constituency = repository.findConstituencyByName("constituency");
        assertThat(constituency.getUuid()).isEqualTo(constituencyUUID);
    }

    @Test()
    public void shouldNotFindConstituencyByNameWhenDoesNotExist() {
        Constituency constituency = repository.findConstituencyByName("invalid");
        assertThat(constituency).isNull();
    }

    @Test()
    public void shouldFindConstituencyByUuidWhenExists() {
        Constituency constituency = repository.findConstituencyByUUID(constituencyUUID);
        assertThat(constituency.getUuid()).isEqualTo(constituencyUUID);
    }

    @Test()
    public void shouldNotFindConstituencyByUuidWhenDoesNotExist() {
        Constituency constituency = repository.findConstituencyByUUID(UUID.randomUUID());
        assertThat(constituency).isNull();
    }
}
