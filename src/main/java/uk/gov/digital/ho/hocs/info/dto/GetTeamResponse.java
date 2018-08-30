package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.entities.Team;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetTeamResponse {

    @JsonProperty("team")
    Team team;

    public static GetTeamResponse from(final Team team) {
        return new GetTeamResponse(team);
    }

}
