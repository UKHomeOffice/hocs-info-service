package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetDeadlinesResponse {

    @JsonProperty("deadlines")
    Set<Deadline> deadlines;

    public static GetDeadlinesResponse from(Set<Deadline> deadlines) {
        return new GetDeadlinesResponse(deadlines);
    }

}
