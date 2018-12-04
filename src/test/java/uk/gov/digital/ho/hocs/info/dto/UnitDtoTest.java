package uk.gov.digital.ho.hocs.info.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;
import uk.gov.digital.ho.hocs.info.entities.Permission;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.entities.Unit;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;


public class UnitDtoTest {

    @Test
    public void shouldReturnUnitWithoutTeams() {

        Set<Permission> permissions = new HashSet<Permission>(){{
            add(new Permission(AccessLevel.OWNER, null,
                    new CaseTypeEntity(1L, "MIN", "type","a1", "role", "DCU_MIN_DISPATCH",  true)));
            add(new Permission(AccessLevel.OWNER, null,
                    new CaseTypeEntity(1L, "MIN", "type", "a1","role","DCU_MIN_DISPATCH",  true)));
        }};
        Team team = new Team("Team 1", UUID.randomUUID(), permissions);
        Unit unit = new Unit("Unit 1", "TEST", UUID.randomUUID(), true);
        unit.addTeam(team);


        UnitDto result = UnitDto.fromWithoutTeams(unit);
        assertThat(result.getTeams()).isNull();


    }

    @Test
    public void shouldReturnUnitWithTeamsAndPermissions() {

        Set<Permission> permissions = new HashSet<Permission>(){{
            add(new Permission(AccessLevel.OWNER, null,
                    new CaseTypeEntity(1L, "MIN", "type","a1", "role","DCU_MIN_DISPATCH",  true)));
            add(new Permission(AccessLevel.OWNER, null,
                    new CaseTypeEntity(1L, "MIN", "type","a1", "role","DCU_MIN_DISPATCH",  true)));
        }};
        Team team = new Team("Team 1", UUID.randomUUID(), permissions);
        Unit unit = new Unit("Unit 1", "TEST", UUID.randomUUID(), true);
        unit.addTeam(team);

        TeamDto teamDto = TeamDto.from(team);


        UnitDto result = UnitDto.from(unit);
        assertThat(result.getTeams().size()).isEqualTo(1);
        assertThat(result.getTeams()).contains(teamDto);
    }
}