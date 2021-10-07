package uk.gov.digital.ho.hocs.info.domain.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.domain.repository.EntityRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class EntityRepositoryTest {

    private final EntityList entityList =
            new EntityList(UUID.randomUUID(), "Test Name", "TEST_NAME", true);
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private EntityRepository repository;

    @Before
    public void setup() {
        entityManager.persistAndFlush(entityList);
    }

    @After
    public void teardown() {
        entityManager.clear();
    }

    @Test()
    public void shouldReturnList_bySortOrder() {
        List<Entity> entities = List.of(
                new Entity(UUID.randomUUID(), "simple", "{}", entityList.getUuid(), true, 20),
                new Entity(UUID.randomUUID(), "simple2", "{}", entityList.getUuid(), true, 10)
        );

        entities.forEach(entityManager::persistAndFlush);

        List<Entity> fetchedEntities = repository.findByEntityListSimpleName(entityList.getSimpleName());

        assertThat(fetchedEntities).isEqualTo(
                entities.stream().sorted(Comparator.comparing(Entity::getSortOrder)).collect(Collectors.toList()));
    }
}
