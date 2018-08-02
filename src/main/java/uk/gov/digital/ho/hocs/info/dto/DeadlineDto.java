package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import uk.gov.digital.ho.hocs.info.entities.Deadline;

import java.time.LocalDate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class DeadlineDto {

    @JsonProperty("displayName")
    private LocalDate date;

    @JsonProperty("type")
    private String type;

    public static DeadlineDto from(Deadline deadline){
        return new DeadlineDto(deadline.getDate(), deadline.getType());
    }
}
