package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.NoArgsConstructor;
import lombok.Setter;

import uk.gov.digital.ho.hocs.info.security.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.Field;

import java.util.UUID;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
public class FieldDto {

    private UUID uuid;
    private String component;
    private String validation;
    private String name;
    private String label;
    private String props;
    private boolean summary;
    private boolean active;
    private AccessLevel accessLevel;
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
