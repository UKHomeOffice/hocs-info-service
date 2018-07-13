package uk.gov.digital.ho.hocs.info.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
public class Deadline {

    @JsonProperty("type")
    private String type;

    @JsonProperty("date")
    private LocalDate date;
}
