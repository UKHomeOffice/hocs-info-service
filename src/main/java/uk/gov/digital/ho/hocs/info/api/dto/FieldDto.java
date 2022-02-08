package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.Field;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
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

    @JsonProperty("accessLevel")
    private AccessLevel accessLevel;

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
                field.getAccessLevel(),
                childField);
    }

    public static FieldDto fromWithDecoratedProps(Field field, ObjectMapper mapper) throws JsonProcessingException {
        mapper.writeValueAsString(field);
        return null;
    }
}
