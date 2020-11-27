package uk.gov.digital.ho.hocs.info.domain.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.domain.model.SomuType;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class SomuTypeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SomuTypeRepository repository;

    @Test()
    public void findAllActive() {
        var somuType1 = new SomuType("CaseType1", "Type1", "{}", false);
        var somuType2 = new SomuType("CaseType2", "Type2", "{}", true);
        entityManager.persist(somuType1);
        entityManager.persist(somuType2);

        var somuTypes = repository.findAll();

        assertThat(somuTypes).isNotNull();
        assertThat(somuTypes.size()).isGreaterThan(0);
    }

    @Test()
    public void findAllByCaseType() {
        var somuType1 = new SomuType("CaseType3", "Type3", "{}", false);
        var somuType2 = new SomuType("CaseType4", "Type4", "{}", true);
        entityManager.persist(somuType1);
        entityManager.persist(somuType2);

        var somuTypes = repository.findAllByCaseType("CaseType3");

        assertThat(somuTypes).isNotNull();
        assertThat(somuTypes.size()).isEqualTo(1);
    }

    @Test()
    public void findByCaseTypeAndType() {
        var somuType1 = new SomuType("CaseType5", "Type5", "{}", false);
        var somuType2 = new SomuType("CaseType6", "Type6", "{}", true);
        entityManager.persist(somuType1);
        entityManager.persist(somuType2);

        var somuType = repository.findByCaseTypeAndType("CaseType5", "Type5");

        assertThat(somuType).isNotNull();
        assertThat(somuType.getCaseType()).isEqualTo("CaseType5");
        assertThat(somuType.getType()).isEqualTo("Type5");
    }
}
