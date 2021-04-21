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
import uk.gov.digital.ho.hocs.info.security.AccessLevel;
import javax.persistence.PersistenceException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TeamRepositoryTest {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeamRepository repository;

    UUID unitUUID;
    UUID teamUUID;
    UUID topicUUID;

    @Before
    public void setup() {
        Unit unit = new Unit("Unit 1", "UNIT_1",true);
        CaseType caseType = new CaseType(null,UUID.randomUUID(),"TEST","a5","TEST",unit.getUuid(),"TEST", true, true);
        entityManager.persistAndFlush(unit);
        entityManager.persistAndFlush(caseType);
        Set<Permission> permissions = new HashSet<Permission>(){{
            add(new Permission(AccessLevel.OWNER,null, caseType));
        }};
        Team team = new Team("a team", permissions);
        team.setActive(true);
        unit.addTeam(team);
        this.entityManager.persist(unit);
        StageTypeEntity stage = new StageTypeEntity(UUID.randomUUID(),"Stage","c","stageType",caseType.getUuid(),1,1,1,true,team);
        entityManager.persistAndFlush(stage);
        TeamLink teamLink = new TeamLink("linkValue", "TEXT", team.getUuid(), "TEST", "stageType");
        entityManager.persistAndFlush(teamLink);
        ParentTopic parentTopic = new ParentTopic("__ParentTopic__");
        Topic topic = new Topic("__Topic__", parentTopic.getUuid());
        this.entityManager.persist(topic);
        this.entityManager.persist(parentTopic);
        TeamLink teamLink2 = new TeamLink(topic.getUuid().toString(), "TOPIC", team.getUuid(), "TEST", "stageType");
        entityManager.persistAndFlush(teamLink2);
        teamUUID = team.getUuid();
        unitUUID = unit.getUuid();
        topicUUID = topic.getUuid();
    }

    @Test()
    public void shouldGetAllTeamsForAUnit() {
        List<Team> teams = new ArrayList<>(repository.findTeamsByUnitUuid(unitUUID));
        assertThat(teams.get(0).getUnit().getUuid()).isEqualTo(unitUUID);
        assertThat(teams.get(0).getUuid()).isEqualTo(teamUUID);
        assertThat(teams.size()).isEqualTo(1);
    }

    @Test()
    public void shouldFindTeamByUUID() {
        Team team = repository.findByUuid(teamUUID);
        assertThat(team.getUnit().getUuid()).isEqualTo(unitUUID);
        assertThat(team.getUuid()).isEqualTo(team.getUuid());
    }

    @Test()
    public void shouldGetPermissions() {
        Team team = repository.findByUuid(teamUUID);
        assertThat(team.getPermissions().size()).isEqualTo(1);
        List<Permission> permissions = new ArrayList<>(team.getPermissions());
        assertThat(permissions.get(0).getCaseType().getType()).isEqualTo("TEST");
        assertThat(permissions.get(0).getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(permissions.get(0).getTeam().getUuid()).isEqualTo(team.getUuid());
    }

    @Test(expected = PersistenceException.class)
    public void shouldThrowExceptionWhenuplicateDisplayName() {
        UUID teamUUID = UUID.randomUUID();
        entityManager.persist(new Team("test 1", true));
        entityManager.persist(new Team("test 1", true));
    }

    @Test
    public void addPermissionsShouldBeIdempotent() {
        Team team = repository.findByUuid(teamUUID);
        assertThat(team.getPermissions().size()).isEqualTo(1);
        CaseType caseType = new CaseType(null,UUID.randomUUID(),"TEST","c7","TEST", UUID.randomUUID(),"TEST", true, true);
        team.addPermission(new Permission(AccessLevel.OWNER,null, caseType));
        assertThat(team.getPermissions().size()).isEqualTo(1);

    }

    @Test()
    public void shouldFindByStageAndText() {
        Team team = repository.findByStageAndText("stageType","linkValue");

        assertThat(team).isNotNull();
        assertThat(team.getUuid()).isEqualTo(teamUUID);
    }

    @Test
    public void shouldFindTeamsByTopicUuid() {
        Set<Team> teams = repository.findTeamsByTopicUuid(topicUUID);

        assertThat(teams).isNotNull();
        assertThat(teams.size()).isEqualTo(1);
        assertThat(teams.iterator().next().getUuid()).isEqualTo(teamUUID);
    }

    @Test()
    public void shouldFindTeamByDisplayName() {
        Team team = repository.findByDisplayName("a team");
        assertThat(team.getUnit().getUuid()).isEqualTo(unitUUID);
        assertThat(team.getUuid()).isEqualTo(team.getUuid());
    }

}