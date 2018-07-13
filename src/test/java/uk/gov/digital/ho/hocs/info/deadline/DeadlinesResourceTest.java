package uk.gov.digital.ho.hocs.info.deadline;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.GetDeadlinesRequest;
import uk.gov.digital.ho.hocs.info.dto.GetDeadlinesResponse;
import uk.gov.digital.ho.hocs.info.dto.Deadline;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeadlinesResourceTest {

    @Mock
    private DeadlinesService deadlinesService;

    private DeadlinesResource deadlinesResource;

    @Before
    public void setUp() {
        deadlinesResource = new DeadlinesResource(deadlinesService);
    }

    @Test
    public void shouldReturnDeadlineDates() {

        when(deadlinesService.getDeadlines(any(), any())).thenReturn(getMockDeadlines());

        ResponseEntity<GetDeadlinesResponse> response = deadlinesResource.getDeadlines(new GetDeadlinesRequest("MIN",LocalDate.of(2018, 01, 18)));

        verify(deadlinesService, times(1)).getDeadlines("MIN",LocalDate.of(2018, 01, 18));

        List<Deadline> responseEntityAsList = new ArrayList<>(response.getBody().getDeadlines());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Deadline result1 = responseEntityAsList.stream().filter(x -> "draft".equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDate()).isEqualTo(LocalDate.of(2018, 01, 18));

        Deadline result2 = responseEntityAsList.stream().filter(x -> "dispatch".equals(x.getType())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getDate()).isEqualTo(LocalDate.of(2018, 01, 25));

    }

    private Set<Deadline> getMockDeadlines() {
        Deadline deadline1 = new Deadline();
        deadline1.setDate(LocalDate.of(2018, 01, 18));
        deadline1.setType("draft");
        Deadline deadline2 = new Deadline();
        deadline2.setDate(LocalDate.of(2018, 01, 25));
        deadline2.setType("dispatch");
        Set<Deadline> deadlines = new HashSet<>();
        deadlines.add(deadline1);
        deadlines.add(deadline2);
        return deadlines;
    }
}