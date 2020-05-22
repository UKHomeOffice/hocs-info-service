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
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;
import uk.gov.digital.ho.hocs.info.domain.model.DocumentTag;
import uk.gov.digital.ho.hocs.info.domain.model.Unit;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class DocumentTagRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DocumentTagRepository repository;

    @Before
    public void setup() {
        Unit unit = new Unit("Unit 1", "UNIT_1",true);
        this.entityManager.persist(unit);
        CaseType caseType = new CaseType("displayName","short","TEST",unit.getUuid(),"deadline",false,true);
        this.entityManager.persist(caseType);
        DocumentTag documentTag;
        documentTag = new DocumentTag(null,UUID.randomUUID(),caseType.getUuid(),"tag3",(short)3);
        this.entityManager.persist(documentTag);
        documentTag = new DocumentTag(null,UUID.randomUUID(),caseType.getUuid(),"tag1",(short)1);
        this.entityManager.persist(documentTag);
        documentTag = new DocumentTag(null,UUID.randomUUID(),caseType.getUuid(),"tag2",(short)2);
        this.entityManager.persist(documentTag);
    }

    @Test()
    public void shouldInsertUnitWithUUID() {
        List<DocumentTag> documentTags = repository.findByCaseType("TEST");
        assertThat(documentTags.size()).isEqualTo(3);
        assertThat(documentTags.get(0).getTag()).isEqualTo("tag1");
        assertThat(documentTags.get(1).getTag()).isEqualTo("tag2");
        assertThat(documentTags.get(2).getTag()).isEqualTo("tag3");
    }
}
