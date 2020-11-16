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

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TopicRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TopicRepository repository;

    @Test()
    public void shouldFindTopicsByCaseType() {

        ParentTopic parentTopic = new ParentTopic("__ParentTopic__");
        Topic topic = new Topic("__Topic__", parentTopic.getUuid());
        Unit unit = new Unit("__UNIT__", "__U1__", true);
        CaseType caseType = new CaseType("__CASETYPE__", "CT", "__CASETYPE__", unit.getUuid(), "__STAGETYPE__", false, true);
        Team team = new Team("__Team__", true);
        team.setUnit(unit);
        StageTypeEntity stageTypeEntity = new StageTypeEntity(UUID.randomUUID(), "__STAGETYPE__", "__STAGETYPE__", "__STAGETYPE__", caseType.getUuid(), 1,1,1,true, team);
        TeamLink teamLink = new TeamLink(topic.getUuid().toString(), "TOPIC", team.getUuid(), "__CASETYPE__", "__STAGETYPE__");
        this.entityManager.persist(topic);
        this.entityManager.persist(parentTopic);
        this.entityManager.persist(unit);
        this.entityManager.persist(caseType);
        this.entityManager.persist(team);
        this.entityManager.persist(stageTypeEntity);
        this.entityManager.persist(teamLink);

        List<Topic> topics = repository.findTopicsByCaseType("__CASETYPE__");

        assertThat(topics.size()).isEqualTo(1);
        assertThat(topics.get(0).getDisplayName()).isEqualTo("__Topic__");
    }

    @Test()
    public void shouldGetAllActiveAssignedTopics() {

        ParentTopic parentTopic1 = new ParentTopic("ParentTopic1");
        ParentTopic parentTopic2 = new ParentTopic("ParentTopic2");
        parentTopic2.setActive(false);
        Topic childTopic1 = new Topic("ChildTopic1", parentTopic1.getUuid());
        Topic childTopic2 = new Topic("ChildTopic2", parentTopic1.getUuid());
        Topic childTopic3 = new Topic("ChildTopic3", parentTopic1.getUuid());
        childTopic2.setActive(false);
        Topic childTopic4 = new Topic("ChildTopic4", parentTopic2.getUuid());
        Topic childTopic5 = new Topic("ChildTopic5", parentTopic2.getUuid());
        childTopic5.setActive(false);
        Unit unit = new Unit("__UNIT1__", "__U1__", true);
        CaseType caseType = new CaseType("__CASETYPE__", "CT", "__CASETYPE__", unit.getUuid(), "__STAGETYPE__", false, true);
        Team team = new Team("__Team1__", true);
        team.setUnit(unit);
        StageTypeEntity stageTypeEntity = new StageTypeEntity(UUID.randomUUID(), "__STAGETYPE__", "__STAGETYPE__", "__STAGETYPE__", caseType.getUuid(), 1,1,1,true, team);
        TeamLink teamLink1 = new TeamLink(childTopic1.getUuid().toString(), "TOPIC", team.getUuid(), "__CASETYPE__", "__STAGETYPE__");
        TeamLink teamLink2 = new TeamLink(childTopic2.getUuid().toString(), "TOPIC", team.getUuid(), "__CASETYPE__", "__STAGETYPE__");
        TeamLink teamLink3 = new TeamLink(childTopic3.getUuid().toString(), "TOPIC", team.getUuid(), "__CASETYPE__", "__STAGETYPE__");
        TeamLink teamLink4 = new TeamLink(childTopic5.getUuid().toString(), "TOPIC", team.getUuid(), "__CASETYPE__", "__STAGETYPE__");

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
        this.entityManager.persist(teamLink3);
        this.entityManager.persist(teamLink4);

        List<Topic> topics = repository.findAllActiveAssignedTopicsByCaseType("__CASETYPE__");

        assertThat(topics.size()).isEqualTo(2);
        assertThat(topics.get(0).getDisplayName()).isEqualTo("ChildTopic1");
        assertThat(topics.get(1).getDisplayName()).isEqualTo("ChildTopic3");
    }
    
    @Test()
    public void shouldFindAllActiveTopicsByTeams() {
        ParentTopic parentTopic1 = new ParentTopic("ParentTopic1");
        ParentTopic parentTopic2 = new ParentTopic("ParentTopic2");
        parentTopic2.setActive(false);
        Topic childTopic1 = new Topic("ChildTopic1", parentTopic1.getUuid());
        Topic childTopic2 = new Topic("ChildTopic2", parentTopic1.getUuid());
        childTopic2.setActive(false);
        
        Unit unit = new Unit("__UNIT1__", "__U1__", true);
        CaseType caseType = new CaseType("__CASETYPE__", "CT", "__CASETYPE__", unit.getUuid(), "__STAGETYPE__", false, true);
        Team team = new Team("__Team1__", true);
        team.setUnit(unit);
        
        StageTypeEntity stageTypeEntity = new StageTypeEntity(UUID.randomUUID(), "__STAGETYPE__", "__STAGETYPE__", "__STAGETYPE__", caseType.getUuid(), 1,1,1,true, team);
        TeamLink teamLink1 = new TeamLink(childTopic1.getUuid().toString(), "TOPIC", team.getUuid(), "__CASETYPE__", "__STAGETYPE__");
        TeamLink teamLink2 = new TeamLink(childTopic2.getUuid().toString(), "TOPIC", team.getUuid(), "__CASETYPE__", "__STAGETYPE__");
       
        this.entityManager.persist(childTopic1);
        this.entityManager.persist(childTopic2);
        this.entityManager.persist(parentTopic1);
        this.entityManager.persist(parentTopic2);
        this.entityManager.persist(unit);
        this.entityManager.persist(caseType);
        this.entityManager.persist(team);
        this.entityManager.persist(stageTypeEntity);
        this.entityManager.persist(teamLink1);
        this.entityManager.persist(teamLink2);
      
        List<Topic> topics = repository.findAllActiveTopicsByTeams(List.of(team.getUuid()));

        assertThat(topics).isNotNull();
        assertThat(topics.size()).isEqualTo(1);
        assertThat(topics.get(0).getDisplayName()).isEqualTo("ChildTopic1");
    }

    @Test()
    public void shouldFindAllActiveTopicsByTeams_NoTopics_ReturnsEmpty() {
        Unit unit = new Unit("__UNIT1__", "__U1__", true);
        Team team = new Team("__Team1__", true);
        team.setUnit(unit);

        this.entityManager.persist(unit);
        this.entityManager.persist(team);

        List<Topic> topics = repository.findAllActiveTopicsByTeams(List.of(team.getUuid()));

        assertThat(topics).isNotNull();
        assertThat(topics.size()).isEqualTo(0);
    }
}
