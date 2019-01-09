package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.Field;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class FieldDto {

    @JsonProperty("uuid")
    private UUID uuid;

    @JsonRawValue
    private String data;

    @JsonProperty("summary")
    private boolean summary;

    @JsonProperty("active")
    private boolean active;

    public static FieldDto from(Field field) {
        return new FieldDto(field.getUuid(), field.getData(), field.isSummary(), field.isActive());
    }
}
