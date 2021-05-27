package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor()
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class PatchTeamDto {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("unitUuid")
    private UUID unitUuid;
}
