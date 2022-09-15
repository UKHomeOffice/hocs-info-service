package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import uk.gov.digital.ho.hocs.info.domain.model.Field;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
@Slf4j
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

        return new FieldDto(field.getUuid(), field.getComponent(), field.getValidation(), field.getName(),
            field.getLabel(), field.getProps(), field.isSummary(), field.isActive(), field.getAccessLevel(),
            childField);
    }

    @Deprecated(forRemoval = true)
    public static FieldDto fromWithDecoratedProps(final Field field, ObjectMapper mapper) {
        final FieldDto childField = field.getChild() != null ? FieldDto.from(field.getChild()) : null;

        String props = field.getProps();

        if (!Objects.equals(props, "")) {
            try {
                Map<String, String> propsMap = mapper.readValue(field.getProps(), Map.class);

                if (field.getLabel() != null && field.getName() != null) {
                    propsMap.put("label", field.getLabel());
                    propsMap.put("name", field.getName());
                }

                props = mapper.writeValueAsString(propsMap);

            } catch (JsonProcessingException e) {
                log.error("Error processing props on field {}", field.getUuid());
            }
        }

        return new FieldDto(field.getUuid(), field.getComponent(), field.getValidation(), field.getName(),
            field.getLabel(), props, field.isSummary(), field.isActive(), field.getAccessLevel(), childField);
    }

}
