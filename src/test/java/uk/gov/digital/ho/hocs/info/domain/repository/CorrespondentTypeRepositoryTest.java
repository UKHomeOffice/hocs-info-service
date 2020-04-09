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
public class CorrespondentTypeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CorrespondentTypeRepository repository;

    UUID unitUUID;
    UUID caseTypeUUID;
    UUID correspondentTypeUUID;

    @Before
    public void setup() {
        Unit unit = new Unit("Unit 1", "UNIT_1",true);
        CaseType caseType = new CaseType(null,UUID.randomUUID(),"TEST","a5","CASE_TYPE",unit.getUuid(),"TEST", true, true);
        entityManager.persistAndFlush(unit);
        entityManager.persistAndFlush(caseType);
        CorrespondentType correspondentType = new CorrespondentType(null, UUID.randomUUID(), "Test", "TEST");
        entityManager.persistAndFlush(correspondentType);
        unitUUID = unit.getUuid();
        caseTypeUUID = caseType.getUuid();
        correspondentTypeUUID = correspondentType.getUuid();
        CaseTypeCorrespondentType caseTypeCorrespondentType = new CaseTypeCorrespondentType(null, caseTypeUUID, correspondentTypeUUID);
        entityManager.persistAndFlush(caseTypeCorrespondentType);
    }

    @Test()
    public void findAllByCaseType() {

        List<CorrespondentType> correspondentTypes = new ArrayList<>(repository.findAllByCaseType("CASE_TYPE"));

        assertThat(correspondentTypes.get(0).getUuid()).isEqualTo(correspondentTypeUUID);
        assertThat(correspondentTypes.get(0).getDisplayName()).isEqualTo("Test");
        assertThat(correspondentTypes.get(0).getType()).isEqualTo("TEST");
        assertThat(correspondentTypes.size()).isEqualTo(1);
    }
}
