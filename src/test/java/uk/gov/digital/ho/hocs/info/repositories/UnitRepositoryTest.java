package uk.gov.digital.ho.hocs.info.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.entities.Unit;
import javax.persistence.PersistenceException;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UnitRepositoryTest {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UnitRepository repository;

    private final UUID unitUUID = UUID.randomUUID();

    @Before
    public void setup() {
        this.entityManager.persist(new Unit("Unit 1", "UNIT_1", unitUUID,true));
    }

    @Test()
    public void shouldInsertUnitWithUUID() {
        Unit unit = repository.findByUuid(unitUUID);
        assertThat(unit.getShortCode()).isEqualTo("UNIT_1");
        assertThat(unit.getDisplayName()).isEqualTo("Unit 1");
    }

    @Test(expected = PersistenceException.class)
    public void shouldThrowExceptionWhenuplicateUUID() {
        this.entityManager.persist(new Unit("Unit 1", "UNIT_1", unitUUID,true));

    }

}