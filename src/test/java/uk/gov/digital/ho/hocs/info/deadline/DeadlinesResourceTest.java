package uk.gov.digital.ho.hocs.info.deadline;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.DeadlineDto;
import uk.gov.digital.ho.hocs.info.dto.GetDeadlinesRequest;
import uk.gov.digital.ho.hocs.info.dto.GetDeadlinesResponse;

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
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public void shouldReturnDeadlineDates() {

        when(deadlinesService.getDeadlines(any(), any())).thenReturn(getMockDeadlines());

        ResponseEntity<GetDeadlinesResponse> response = deadlinesResource.getDeadlines(new GetDeadlinesRequest("MIN",LocalDate.of(2018, 01, 18)));

        verify(deadlinesService, times(1)).getDeadlines("MIN",LocalDate.of(2018, 01, 18));

        List<DeadlineDto> responseEntityAsList = new ArrayList<>(response.getBody().getDeadlineDtos());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DeadlineDto result1 = responseEntityAsList.stream().filter(x -> "draft".equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDate()).isEqualTo(LocalDate.of(2018, 01, 18));

        DeadlineDto result2 = responseEntityAsList.stream().filter(x -> "dispatch".equals(x.getType())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getDate()).isEqualTo(LocalDate.of(2018, 01, 25));

    }

    private Set<DeadlineDto> getMockDeadlines() {
        Set<DeadlineDto> deadlineDtos = new HashSet<>();
        deadlineDtos.add(new DeadlineDto("draft", LocalDate.of(2018, 01, 18)));
        deadlineDtos.add(new DeadlineDto("dispatch",LocalDate.of(2018, 01, 25)));
        return deadlineDtos;
    }
}