package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.Deadline;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetDeadlinesResponse {

    @JsonProperty("deadlines")
    Map<String, LocalDate> deadlines;

    public static GetDeadlinesResponse from(Set<Deadline> deadlinesSet) {
        Map<String, LocalDate> deadlines = new HashMap<>();

        deadlinesSet.forEach(deadline -> deadlines.put(deadline.getType(), deadline.getDate()));

        return new GetDeadlinesResponse(deadlines);
    }
}
