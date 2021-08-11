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

    @Test()
    public void shouldGetAllParentTopicsWithActiveAssignedChildren() {

        ParentTopic parentTopic1 = new ParentTopic("ParentTopic1");
        ParentTopic parentTopic2 = new ParentTopic("ParentTopic2");
        ParentTopic parentTopic3 = new ParentTopic("ParentTopic3");
        parentTopic3.setActive(false);
        Topic childTopic1 = new Topic("ChildTopic1", parentTopic1.getUuid());
        Topic childTopic2 = new Topic("ChildTopic2" , parentTopic1.getUuid());
        childTopic2.setActive(false);
        Topic childTopic3 = new Topic("ChildTopic3" , parentTopic2.getUuid());
        Topic childTopic4 = new Topic("ChildTopic4" , parentTopic2.getUuid());
        Topic childTopic5 = new Topic("ChildTopic5" , parentTopic3.getUuid());
        Unit unit = new Unit("__UNIT1__", "__U1__", true);
        CaseType caseType = new CaseType("__CASETYPE__", "CT", "__CASETYPE__", unit.getUuid(), "__STAGETYPE__", false, true, null);
        Team team = new Team("__Team1__", true);
        team.setUnit(unit);
        StageTypeEntity stageTypeEntity = new StageTypeEntity(UUID.randomUUID(), "__STAGETYPE__", "__STAGETYPE__", "__STAGETYPE__", caseType.getUuid(), 1,1,1,true, team);
        TeamLink teamLink1 = new TeamLink(childTopic1.getUuid().toString(), "TOPIC", team.getUuid(), "__CASETYPE__", "__STAGETYPE__");
        TeamLink teamLink2 = new TeamLink(childTopic5.getUuid().toString(), "TOPIC", team.getUuid(), "__CASETYPE__", "__STAGETYPE__");

        this.entityManager.persist(childTopic1);
        this.entityManager.persist(childTopic2);
        this.entityManager.persist(childTopic3);
        this.entityManager.persist(childTopic4);
        this.entityManager.persist(childTopic5);
        this.entityManager.persist(parentTopic1);
        this.entityManager.persist(parentTopic2);
        this.entityManager.persist(unit);
        this.entityManager.persist(caseType);
        this.entityManager.persist(team);
        this.entityManager.persist(stageTypeEntity);
        this.entityManager.persist(teamLink1);
        this.entityManager.persist(teamLink2);

        List<ParentTopic> parentTopics = repository.findAllParentTopicByCaseType("__CASETYPE__");

        assertThat(parentTopics.size()).isEqualTo(1);
        assertThat(parentTopics.get(0).getDisplayName()).isEqualTo("ParentTopic1");
    }
}