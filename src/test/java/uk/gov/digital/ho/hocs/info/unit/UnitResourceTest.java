package uk.gov.digital.ho.hocs.info.unit;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.GetUnitsResponse;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.entities.Unit;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
public class UnitResourceTest {

    @Mock
    private UnitService unitService;

    private UnitResource unitResource;

    private static final String[] ROLES = {"Create", "DCU"};

    @Before
    public void setUp() {
        unitResource = new UnitResource(unitService);
    }

    @Test
    public void shouldGetUnitsAndTeamsByTenant() {

        when(unitService.getUnits(any())).thenReturn(getUnits());

        ResponseEntity<GetUnitsResponse> response = unitResource.getAllUnits(ROLES);

        verify(unitService, times(1)).getUnits(anyList());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getUnits().size()).isEqualTo(1);
        assertThat(response.getBody().getUnits().get(0).getDisplayName()).isEqualTo("Unit 1");

        List<Team> responseTeamAsList = new ArrayList<>(response.getBody().getUnits().get(0).getTeam());

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