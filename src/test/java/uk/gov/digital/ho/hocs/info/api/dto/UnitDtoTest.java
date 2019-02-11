package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;
import uk.gov.digital.ho.hocs.info.domain.model.Permission;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.model.Unit;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class UnitDtoTest {

    @Test
    public void shouldReturnUnitWithoutTeams() {

        Set<Permission> permissions = new HashSet<Permission>(){{
            add(new Permission(AccessLevel.OWNER, null,
                    new CaseType(1L,UUID.randomUUID(), "MIN", "type","a1", UUID.randomUUID(), "DCU_MIN_DISPATCH", true,  true)));
            add(new Permission(AccessLevel.OWNER, null,
                    new CaseType(1L,UUID.randomUUID(), "MIN", "type", "a1",UUID.randomUUID(),"DCU_MIN_DISPATCH", true,  true)));
        }};
        Team team = new Team("Team 1",  permissions);
        Unit unit = new Unit("Unit 1", "TEST", true);
        unit.addTeam(team);


        UnitDto result = UnitDto.fromWithoutTeams(unit);
        assertThat(result.getTeams()).isNull();


    }

    @Test
    public void shouldReturnUnitWithTeamsAndPermissions() {

        Set<Permission> permissions = new HashSet<Permission>(){{
            add(new Permission(AccessLevel.OWNER, null,
                    new CaseType(1L,UUID.randomUUID(), "MIN", "type","a1", UUID.randomUUID(),"DCU_MIN_DISPATCH", true,  true)));
            add(new Permission(AccessLevel.OWNER, null,
                    new CaseType(1L, UUID.randomUUID(), "MIN", "type","a1", UUID.randomUUID(),"DCU_MIN_DISPATCH", true,  true)));
        }};
        Team team = new Team("Team 1", permissions);
        Unit unit = new Unit("Unit 1", "TEST", true);
        unit.addTeam(team);

        TeamDto teamDto = TeamDto.from(team);


        UnitDto result = UnitDto.from(unit);
        assertThat(result.getTeams().size()).isEqualTo(1);
        assertThat(result.getTeams()).contains(teamDto);
    }
}