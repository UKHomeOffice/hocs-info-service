package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class UpdateTeamNameRequest {


        private String displayName;

        @JsonCreator
        public UpdateTeamNameRequest(@JsonProperty("displayName") String displayName) {
                this.displayName = displayName;
        }
}
