package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.Deadline;

import java.time.LocalDate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class DeadlineDto {

    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("type")
    private String type;

    public static DeadlineDto from(Deadline deadline){
        return new DeadlineDto(deadline.getDate(), deadline.getType());
    }
}
