package uk.gov.digital.ho.hocs.info.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entity.Entity;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class GetCaseSummaryFieldsResponse {

    @JsonProperty("fields")
    Set<String> fields;

    public static GetCaseSummaryFieldsResponse from(Set<Entity> entities) {
        Set<String> fields = entities
                .stream()
                .map(e -> e.getSimpleName())
                .collect(Collectors.toSet());

        return new GetCaseSummaryFieldsResponse(fields);
    }
}
