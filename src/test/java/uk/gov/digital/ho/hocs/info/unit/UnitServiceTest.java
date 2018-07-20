package uk.gov.digital.ho.hocs.info.unit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.entities.Unit;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.UnitRepository;
import uk.gov.digital.ho.hocs.info.tenant.TenantService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UnitServiceTest {

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private TenantService tenantService;

    private UnitService unitService;

    @Before
    public void setUp() {
        this.unitService = new UnitService(unitRepository, tenantService);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowExceptionWhenGetUnitsWithNoRoles() throws EntityNotFoundException {
        unitService.getUnits(null);
    }

    @Test
    public void shouldReturnUnitsByTenant(){
        when(tenantService.getTenantsFromRoles(any())).thenReturn(new ArrayList<String>() {{ add("DCU");}});
        when(unitRepository.findUnitByTenant("DCU")).thenReturn(getUnits());

        List<Unit> units = unitService.getUnits(new ArrayList<String>() {{
            add("DCU");
        }});

        verify(unitRepository, times(1)).findUnitByTenant(any());

        assertThat(units.size()).isEqualTo(1);
        assertThat(units.get(0).getId()).isEqualTo(1);
        assertThat(units.get(0).getDisplayName()).isEqualTo("Unit 1");
        assertThat(units.get(0).getTeam().size()).isEqualTo(2);

        List<Team> responseTeamAsList = new ArrayList<>(units.get(0).getTeam());

        Team result1 = responseTeamAsList.stream().filter(x -> Objects.equals(1 ,x.getId())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDisplayName()).isEqualTo("Team1");
        assertThat(result1.getMembers().size()).isEqualTo(2);
        Team result2 = responseTeamAsList.stream().filter(x -> Objects.equals(2,x.getId())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getDisplayName()).isEqualTo("Team2");
        assertThat(result2.getMembers().size()).isEqualTo(2);
    }

    private List<Unit> getUnits() {
        Set<Member> members1 = new HashSet<Member>() {{
            add(new Member(1, "Member1"));
            add(new Member(2, "Member2"));
        }};
        Set<Member> members2 = new HashSet<Member>() {{
            add(new Member(3, "Member3"));
            add(new Member(4, "Member4"));
        }};
        Set<Team> teams = new HashSet<Team>() {{
            add(new Team(1, "Team1", members1));
            add(new Team(2, "Team2", members2));
        }};

        return new ArrayList<Unit>() {{
            add(new Unit(1, "Unit 1", teams));
        }};
    }
}