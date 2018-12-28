package uk.gov.digital.ho.hocs.info.api.deadline;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.GetDeadlinesResponse;
import uk.gov.digital.ho.hocs.info.domain.model.Deadline;
import uk.gov.digital.ho.hocs.info.domain.model.Sla;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    public void shouldReturnAllDeadlineDatesForCaseType()  {

        when(deadlinesService.getDeadlines(any(), any())).thenReturn(getMockDeadlinesForCaseTypeRequest());

        ResponseEntity<GetDeadlinesResponse> response =
                deadlinesResource.getDeadlines("MIN", "2018-01-18");

        verify(deadlinesService, times(1)).getDeadlines("MIN", LocalDate.of(2018, 01, 18));

        Map<String, LocalDate> deadlines = response.getBody().getDeadlines();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        LocalDate draftDate = deadlines.get("draft");
        assertThat(draftDate).isNotNull();
        assertThat(draftDate).isEqualTo(LocalDate.of(2018, 01, 18));

        LocalDate dispatchDate = deadlines.get("dispatch");
        assertThat(dispatchDate).isNotNull();
        assertThat(dispatchDate).isEqualTo(LocalDate.of(2018, 01, 25));
    }

    @Test
    public void shouldReturnCaseDeadline() {

        when(deadlinesService.getCaseDeadlineForCaseType("MIN", LocalDate.of(2018, 01, 01))).thenReturn(new Deadline(LocalDate.of(2018, 01, 29), new Sla("dispatch", 20, "MIN"), new HashSet<>()));

        ResponseEntity<Deadline> response =
                deadlinesResource.getCaseDeadline("MIN", "2018-01-01");

        verify(deadlinesService, times(1)).getCaseDeadlineForCaseType("MIN", LocalDate.of(2018, 01, 01));

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnDeadlineForRequestedStage() {

        when(deadlinesService.getDeadlineForStage("Draft", LocalDate.of(2018, 01, 01))).thenReturn(new Deadline(LocalDate.of(2018, 01, 29), new Sla("Draft", 0, "MIN"), new HashSet<>()));

        ResponseEntity<Deadline> response =
                deadlinesResource.getDeadlineByStage("Draft", "2018-01-01");

        verify(deadlinesService, times(1)).getDeadlineForStage("Draft", LocalDate.of(2018, 01, 01));

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private static Set<Deadline> getMockDeadlinesForCaseTypeRequest() {
        Set<Deadline> deadLines = new HashSet<>();

        deadLines.add(new Deadline(LocalDate.of(2018, 01, 18), new Sla("draft", 0, "MIN"), new HashSet<>()));
        deadLines.add(new Deadline(LocalDate.of(2018, 01, 25), new Sla("dispatch", 0, "MIN"), new HashSet<>()));
        return deadLines;
    }
}