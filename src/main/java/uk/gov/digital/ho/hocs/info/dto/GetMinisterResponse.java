package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.entities.Minister;
import uk.gov.digital.ho.hocs.info.entities.ParentTopic;

import java.util.List;

@NoArgsConstructor
@Getter
public class GetMinisterResponse {

    @JsonProperty("minister")
    private Minister minister;

    public static GetMinisterResponse from(Minister minister) {
        return new GetMinisterResponse(minister);
    }

    private GetMinisterResponse (Minister minister) {
        this.minister = minister;
    }
}
