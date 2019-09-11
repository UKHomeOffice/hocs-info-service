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

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    UUID constituencyUUID = UUID.randomUUID();
    UUID regionUUID = UUID.randomUUID();

    @Before
    public void setup() {
        Region region = new Region(null, regionUUID, "region", true);
        entityManager.persistAndFlush(region);
        Constituency constituency = new Constituency(null, constituencyUUID, "constituency", regionUUID, region, true);
        entityManager.persistAndFlush(constituency);
        HouseAddress houseAddress = new HouseAddress(null, UUID.randomUUID(), "house", "code", "", "", "", "", "", LocalDate.now(), LocalDate.now());
        entityManager.persistAndFlush(houseAddress);
        Member member = new Member(null, "house", "fullTitle", "extRef", UUID.randomUUID(), LocalDateTime.now(), false, houseAddress.getUuid(), houseAddress, constituencyUUID, constituency.getConstituencyName(), constituency);
        entityManager.persistAndFlush(member);
    }

    @Test()
    public void shouldFindAll() {
        List<Constituency> constituencys = new ArrayList<Constituency>((Collection<? extends Constituency>) repository.findAll());
        assertThat(constituencys.get(constituencys.size()-1).getUuid()).isEqualTo(constituencyUUID);
        assertThat(constituencys.get(constituencys.size()-1).getRegionUUID()).isEqualTo(regionUUID);
        assertThat(constituencys.size()).isGreaterThanOrEqualTo(1);
    }

    @Test()
    public void shouldFindAllActiveIsTrue() {
        List<Constituency> constituencys = new ArrayList<Constituency>((Collection<? extends Constituency>) repository.findAllByActiveIsTrue());
        assertThat(constituencys.get(constituencys.size()-1).getUuid()).isEqualTo(constituencyUUID);
        assertThat(constituencys.size()).isGreaterThanOrEqualTo(1);
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

    @Test()
    public void shouldFindConstituencyByMemberExternalReferenceWhenExists() {
        Constituency constituency = repository.findConstituencyByMemberExternalReference("extRef");
        assertThat(constituency.getUuid()).isEqualTo(constituencyUUID);
    }

    @Test()
    public void shouldNotFindConstituencyByMemberExternalReferenceWhenDoesNotExist() {
        Constituency constituency = repository.findConstituencyByMemberExternalReference("invalid");
        assertThat(constituency).isNull();
    }
}
