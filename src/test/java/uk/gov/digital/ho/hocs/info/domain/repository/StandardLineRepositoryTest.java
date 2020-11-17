package uk.gov.digital.ho.hocs.info.domain.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.domain.model.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class StandardLineRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StandardLineRepository repository;

    private final UUID topicUUID = UUID.randomUUID();
    private final UUID documentUuid = UUID.randomUUID();

    @Test()
    public void shouldFindAllStandardLines() {
        var topic1 = new Topic("Test Topic", UUID.randomUUID());
        var standardLine1 = new StandardLine("Test", topic1.getUuid(), LocalDateTime.now());
        var standardLine2 = new StandardLine("Test2", topic1.getUuid(), LocalDateTime.now());
        standardLine1.setDocumentUUID(documentUuid);
        standardLine2.setDocumentUUID(documentUuid);

        entityManager.persist(topic1);
        entityManager.persist(standardLine1);
        entityManager.persist(standardLine2);
        
        var standardLines = repository.findAllStandardLines();
        assertThat(standardLines).isNotNull();
        assertThat(standardLines.size()).isEqualTo(2);
    }

    @Test()
    public void shouldFindByStandardLinesUuid() {
        var standardLine1 = new StandardLine("Test", topicUUID, LocalDateTime.now());
        standardLine1.setDocumentUUID(documentUuid);

        entityManager.persist(standardLine1);

        var standardLine = repository.findByUuid(standardLine1.getUuid());
        assertThat(standardLine).isNotNull();
        assertThat(standardLine).isEqualTo(standardLine1);
    }

    @Test()
    public void shouldFindByStandardLineExpiry() {
        var standardLine1 = new StandardLine("Test", topicUUID, LocalDateTime.now().plusDays(1));
        var standardLine2 = new StandardLine("Test", topicUUID, LocalDateTime.now().minusDays(1));
        standardLine1.setDocumentUUID(documentUuid);
        standardLine2.setDocumentUUID(documentUuid);

        entityManager.persist(standardLine1);
        entityManager.persist(standardLine2);

        var standardLine = repository.findStandardLinesByExpires(LocalDateTime.now());
        assertThat(standardLine).isNotNull();
        assertThat(standardLine.size()).isEqualTo(1);
        assertThat(standardLine.contains(standardLine1)).isTrue();
    }

    @Test()
    public void shouldFindByStandardLineTopicAndExpiry() {
        var standardLine1 = new StandardLine("Test", topicUUID, LocalDateTime.now().plusDays(1));
        var standardLine2 = new StandardLine("Test", UUID.randomUUID(), LocalDateTime.now().plusDays(1));
        var standardLine3 = new StandardLine("Test", topicUUID, LocalDateTime.now().minusDays(1));
        var standardLine4 = new StandardLine("Test", UUID.randomUUID(), LocalDateTime.now().minusDays(1));
        
        var listStandardLines = 
                List.of(standardLine1, standardLine2, standardLine3, standardLine4);
        
        listStandardLines.forEach(standardLine -> {
            standardLine.setDocumentUUID(documentUuid);
            entityManager.persist(standardLine);
        });
        
        var standardLine = repository.findStandardLinesByTopicAndExpires(topicUUID, LocalDateTime.now());
        assertThat(standardLine).isNotNull();
        assertThat(standardLine).isEqualTo(standardLine1); 
    }

    @Test()
    public void findStandardLinesByTopics() {
        UUID uuid = UUID.randomUUID();
        var standardLine1 = new StandardLine("Test", topicUUID, LocalDateTime.now().plusDays(1));
        var standardLine2 = new StandardLine("Test", uuid, LocalDateTime.now().plusDays(1));
        var standardLine3 = new StandardLine("Test", topicUUID, LocalDateTime.now().minusDays(1));
        var standardLine4 = new StandardLine("Test", UUID.randomUUID(), LocalDateTime.now().minusDays(1));

        var listUuid = List.of(topicUUID, uuid);
        var listStandardLines =
                List.of(standardLine1, standardLine2, standardLine3, standardLine4);

        listStandardLines.forEach(standardLine -> {
            standardLine.setDocumentUUID(documentUuid);
            entityManager.persist(standardLine);
        });

        var standardLines = repository.findStandardLinesByTopics(listUuid);
        assertThat(standardLines).isNotNull();
        assertThat(standardLines.size()).isEqualTo(3);
        assertThat(standardLines.containsAll(List.of(standardLine1, standardLine2, standardLine3))).isTrue();
    }


}
