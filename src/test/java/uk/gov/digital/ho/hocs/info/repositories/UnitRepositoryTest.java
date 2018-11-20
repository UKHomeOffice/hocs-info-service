package uk.gov.digital.ho.hocs.info.repositories;

import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;
import uk.gov.digital.ho.hocs.info.entities.Permission;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.entities.Unit;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;
import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UnitRepositoryTest {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UnitRepository repository;

    private final UUID unitUUID = UUID.randomUUID();

    @Before
    public void setup() {
        this.entityManager.persist(new Unit("Unit 1", "UNIT_1", unitUUID,true));
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
        UUID teamUUID = UUID.randomUUID();

        Team team = new Team("a team", teamUUID, true);
        unit.addTeam(team);
        entityManager.persistAndFlush(unit);
        Unit newUnit = repository.findByUuid(unitUUID);
        List<Team> teams = new ArrayList<>(newUnit.getTeams());

        assertThat(teams.get(0).getUnit()).isEqualTo(unit);
        assertThat(teams.get(0).getUuid()).isEqualTo(teamUUID);
        assertThat(teams.get(0).getId()).isNotNull();
    }

    @Test()
    public void shouldAddTeamWithPermissionsToUnit() {
        UUID teamUUID = UUID.randomUUID();
        Unit unit = repository.findByUuid(unitUUID);
        CaseTypeEntity caseType = new CaseTypeEntity(null,"TEST","TEST", "", true);
        entityManager.persistAndFlush(caseType);


        Set<Permission> permissions = new HashSet<Permission>(){{
            add(new Permission(AccessLevel.OWNER,null, caseType));
        }};
        Team team = new Team("a team", teamUUID, permissions);
        unit.addTeam(team);
        entityManager.persistAndFlush(unit);

        Unit savedUnit = repository.findByUuid(unitUUID);
        List<Team> teams = new ArrayList<>(savedUnit.getTeams());

        List<Permission> savedPermissions = new ArrayList<>(teams.get(0).getPermissions());
        assertThat(savedPermissions.get(0).getTeam()).isEqualTo(team);
        assertThat(savedPermissions.get(0).getAccessLevel()).isEqualTo(AccessLevel.OWNER);
        assertThat(savedPermissions.get(0).getId()).isNotNull();
        assertThat(savedPermissions.get(0).getCaseType()).isEqualTo(caseType);
    }

    @Test(expected = PersistenceException.class)
    public void shouldThrowExceptionWhenuplicateUUID() {
        this.entityManager.persist(new Unit("Unit 1", "UNIT_1", unitUUID,true));
    }

    @Test(expected = PersistenceException.class)
    public void shouldThrowExceptionWhenuplicateShortCode() {
        this.entityManager.persist(new Unit("Unit 1", "UNIT_1", unitUUID,true));
        this.entityManager.persist(new Unit("Unit 2", "UNIT_1", UUID.randomUUID(),true));
    }

    @Test(expected = PersistenceException.class)
    public void shouldThrowExceptionWhenuplicateDisplayName() {
        this.entityManager.persist(new Unit("Unit 1", "UNIT_1", unitUUID,true));
        this.entityManager.persist(new Unit("Unit 1", "UNIT_2", UUID.randomUUID(),true));
    }

}