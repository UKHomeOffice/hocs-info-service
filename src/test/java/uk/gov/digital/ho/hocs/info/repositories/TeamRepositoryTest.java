package uk.gov.digital.ho.hocs.info.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;
import uk.gov.digital.ho.hocs.info.entities.Permission;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.entities.Unit;
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

    private final UUID unitUUID = UUID.randomUUID();
    private final UUID teamUUID = UUID.randomUUID();

    @Before
    public void setup() {
        Unit unit = new Unit("Unit 1", "UNIT_1", unitUUID,true);
        CaseTypeEntity caseType = new CaseTypeEntity(null,"TEST","a5","TEST", "","TEST", true);
        entityManager.persistAndFlush(caseType);
        Set<Permission> permissions = new HashSet<Permission>(){{
            add(new Permission(AccessLevel.OWNER,null, caseType));
        }};
        Team team = new Team("a team", teamUUID, permissions);
        unit.addTeam(team);
        this.entityManager.persist(unit);
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
        assertThat(team.getUuid()).isEqualTo(teamUUID);
        assertThat(team.getId()).isNotNull();
    }

    @Test()
    public void shouldGetPermissions() {
        Team team = repository.findByUuid(teamUUID);
        assertThat(team.getPermissions().size()).isEqualTo(1);
        List<Permission> permissions = new ArrayList<>(team.getPermissions());
        assertThat(permissions.get(0).getCaseType().getType()).isEqualTo("TEST");
        assertThat(permissions.get(0).getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(permissions.get(0).getTeam().getUuid()).isEqualTo(teamUUID);
    }

    @Test(expected = PersistenceException.class)
    public void shouldThrowExceptionWhenuplicateUUID() {
       UUID teamUUID = UUID.randomUUID();
        entityManager.persist(new Team("test 1", teamUUID, true));
        entityManager.persist(new Team("test 2", teamUUID, true));
    }

    @Test(expected = PersistenceException.class)
    public void shouldThrowExceptionWhenuplicateDisplayName() {
        UUID teamUUID = UUID.randomUUID();
        entityManager.persist(new Team("test 1", teamUUID, true));
        entityManager.persist(new Team("test 1", UUID.randomUUID(), true));
    }

    @Test
    public void addPermissionsShouldBeIdempotent() {
        Team team = repository.findByUuid(teamUUID);
        assertThat(team.getPermissions().size()).isEqualTo(1);
        CaseTypeEntity caseType = new CaseTypeEntity(null,"TEST","c7","TEST", "","TEST", true);
        team.addPermission(new Permission(AccessLevel.OWNER,null, caseType));
        assertThat(team.getPermissions().size()).isEqualTo(1);

    }


}