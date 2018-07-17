package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class GetDeadlinesRequest {

    @JsonProperty("caseType")
    @NonNull
    private String caseType;

    @JsonProperty("date")
    @NonNull
    private LocalDate date;

    @JsonProperty("sla")
    private Long sla;

}
