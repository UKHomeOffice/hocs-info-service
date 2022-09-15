package uk.gov.digital.ho.hocs.info.domain.repository;

import java.util.*;

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
import uk.gov.digital.ho.hocs.info.domain.model.Permission;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.model.Unit;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UnitRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UnitRepository repository;

    private UUID unitUUID;

    @Before
    public void setup() {
        Unit unit = new Unit("Unit 1", "UNIT_1", true);
        this.entityManager.persist(unit);
        unitUUID = unit.getUuid();
    }

    @Test()
    public void shouldInsertUnitWithUUID() {
        Unit unit = repository.findByUuid(unitUUID);
        assertThat(unit.getShortCode()).isEqualTo("UNIT_1");
        assertThat(unit.getDisplayName()).isEqualTo("Unit 1");
    }

    @Test()
    public void shouldAddTeamToUnit() {
        Unit unit = repository.findByUuid(unitUUID);

        Team team = new Team("a team", true);
        unit.addTeam(team);
        entityManager.persistAndFlush(unit);
        Unit newUnit = repository.findByUuid(unitUUID);
        List<Team> teams = new ArrayList<>(newUnit.getTeams());

        assertThat(teams.get(0).getUnit()).isEqualTo(unit);
        assertThat(teams.get(0).getUuid()).isEqualTo(team.getUuid());
    }

    @Test()
    public void shouldAddTeamWithPermissionsToUnit() {

        Unit unit = repository.findByUuid(unitUUID);
        CaseType caseType = new CaseType(null, UUID.randomUUID(), "TEST", "TEST", "a1", unitUUID, "TEST", true, true,
            null, null);
        entityManager.persistAndFlush(caseType);

        Set<Permission> permissions = new HashSet<Permission>() {{
            add(new Permission(AccessLevel.OWNER, null, caseType));
        }};
        Team team = new Team("a team", permissions);
        unit.addTeam(team);
        entityManager.persistAndFlush(unit);

        Unit savedUnit = repository.findByUuid(unitUUID);
        List<Team> teams = new ArrayList<>(savedUnit.getTeams());

        List<Permission> savedPermissions = new ArrayList<>(teams.get(0).getPermissions());
        assertThat(savedPermissions.get(0).getTeam()).isEqualTo(team);
        assertThat(savedPermissions.get(0).getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(savedPermissions.get(0).getCaseType()).isEqualTo(caseType);
    }

    @Test(expected = PersistenceException.class)
    public void shouldThrowExceptionWhenuplicateUUID() {
        this.entityManager.persist(new Unit("Unit 1", "UNIT_1", true));
    }

    @Test(expected = PersistenceException.class)
    public void shouldThrowExceptionWhenuplicateShortCode() {
        this.entityManager.persist(new Unit("Unit 1", "UNIT_1", true));
        this.entityManager.persist(new Unit("Unit 2", "UNIT_1", true));
    }

    @Test(expected = PersistenceException.class)
    public void shouldThrowExceptionWhenuplicateDisplayName() {
        this.entityManager.persist(new Unit("Unit 1", "UNIT_1", true));
        this.entityManager.persist(new Unit("Unit 1", "UNIT_2", true));
    }

}