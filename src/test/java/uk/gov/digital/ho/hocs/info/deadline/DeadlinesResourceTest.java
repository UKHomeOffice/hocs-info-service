package uk.gov.digital.ho.hocs.info.deadline;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.DeadlineDto;
import uk.gov.digital.ho.hocs.info.dto.GetDeadlinesResponse;
import uk.gov.digital.ho.hocs.info.entities.Deadline;
import uk.gov.digital.ho.hocs.info.entities.Sla;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    public void shouldReturnDeadlineDatesforCaseType() throws EntityPermissionException, EntityNotFoundException {

        when(deadlinesService.getDeadlines(any(), any())).thenReturn(getMockDeadlinesForCaseTypeRequest());

        ResponseEntity<GetDeadlinesResponse> response =
                deadlinesResource.getDeadlines("MIN","2018-01-18");

        verify(deadlinesService, times(1)).getDeadlines("MIN",LocalDate.of(2018, 01, 18));

        List<DeadlineDto> responseEntityAsList = new ArrayList<>(response.getBody().getDeadlines());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DeadlineDto result1 = responseEntityAsList.stream().filter(x -> "draft".equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDate()).isEqualTo(LocalDate.of(2018, 01, 18));

        DeadlineDto result2 = responseEntityAsList.stream().filter(x -> "dispatch".equals(x.getType())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getDate()).isEqualTo(LocalDate.of(2018, 01, 25));

    }

    private static Set<Deadline> getMockDeadlinesForCaseTypeRequest() {
        Set<Deadline> deadLines = new HashSet<>();

        deadLines.add(new Deadline(LocalDate.of(2018, 01, 18), new Sla("draft", 0,"MIN"), new HashSet<>()));
        deadLines.add(new Deadline(LocalDate.of(2018, 01, 25), new Sla("dispatch", 0,"MIN"), new HashSet<>()));
        return deadLines;
    }
}