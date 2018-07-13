package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetDeadlinesRequest {

    @JsonProperty("caseType")
    private String caseType;

    @JsonProperty("date")
    private LocalDate date;

}
