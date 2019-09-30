package uk.gov.digital.ho.hocs.info.domain.repository;

import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.domain.model.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ParentTopicRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ParentTopicRepository repository;

    @Test()
    public void shouldGetAllParentTopics() {
        List<ParentTopic> parentTopics = repository.findAll();

        int initialParentTopicCount = parentTopics.size();

        ParentTopic parentTopic1 = new ParentTopic("ParentTopic1");
        this.entityManager.persist(parentTopic1);
        ParentTopic parentTopic2 = new ParentTopic("ParentTopic2");
        this.entityManager.persist(parentTopic2);

        assertThat(repository.findAll().size()).isEqualTo(initialParentTopicCount + 2);
    }

}