package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonProperty("name")
    private String name;

    @JsonProperty("label")
    private String label;

    @JsonRawValue
    private String props;

    @JsonProperty("summary")
    private boolean summary;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("child")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FieldDto child;

    public static FieldDto from(final Field field) {
        final FieldDto childField = field.getChild() != null ? FieldDto.from(field.getChild()) : null;

        return new FieldDto(field.getUuid(),
                field.getComponent(),
                field.getValidation(),
                field.getName(),
                field.getLabel(),
                field.getProps(),
                field.isSummary(),
                field.isActive(),
                childField);
    }
}
