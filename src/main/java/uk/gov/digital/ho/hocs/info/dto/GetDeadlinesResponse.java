package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.Deadline;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetDeadlinesResponse {

    @JsonProperty("deadlines")
    Set<DeadlineDto> deadlines;

    public static GetDeadlinesResponse from(Set<Deadline> deadlinesSet) {
        Set<DeadlineDto> deadlineDtos = deadlinesSet.stream().map(DeadlineDto::from).collect(Collectors.toSet());
        return new GetDeadlinesResponse(deadlineDtos);
    }
}
