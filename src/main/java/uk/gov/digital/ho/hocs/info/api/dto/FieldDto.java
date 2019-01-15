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

    @JsonProperty("component")
    private String component;

    @JsonRawValue
    private String validation;

    @JsonProperty("props")
    private PropertyDto properties;

    @JsonProperty("summary")
    private boolean summary;

    @JsonProperty("active")
    private boolean active;

    public static FieldDto from(Field field) {
        PropertyDto propertyDto = PropertyDto.from(field.getName(), field.getLabel(), field.getChoices());
        return new FieldDto(field.getUuid(), field.getComponent(), field.getValidation(), propertyDto, field.isSummary(), field.isActive());
    }
}
