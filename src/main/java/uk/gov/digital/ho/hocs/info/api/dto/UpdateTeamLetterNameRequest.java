package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class UpdateTeamLetterNameRequest {

    private String letterName;

    @JsonCreator
    public UpdateTeamLetterNameRequest(@JsonProperty("letterName") String letterName) {
        this.letterName = letterName;
    }

}
