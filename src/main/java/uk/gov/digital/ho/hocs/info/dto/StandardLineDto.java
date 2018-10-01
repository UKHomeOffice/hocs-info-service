package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.entities.StandardLine;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StandardLineDto {

    @JsonProperty("label")
    private String displayName;

    @JsonProperty("value")
    private UUID uuid;

    public static StandardLineDto from(StandardLine standardLine) {
        return new StandardLineDto(standardLine.getDisplayName(), standardLine.getUuid());
    }
}
