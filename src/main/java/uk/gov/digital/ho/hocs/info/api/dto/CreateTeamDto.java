package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Set;

@Getter
@EqualsAndHashCode
public class CreateTeamDto {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("caseTypes")
    private Set<String> caseTypes;

    @JsonCreator
    public CreateTeamDto(@JsonProperty("displayName") String displayName , @JsonProperty("caseTypes")  Set<String> caseTypes ){
        this.displayName = displayName;
        this.caseTypes = caseTypes;
    }
}

