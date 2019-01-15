package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.Schema;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SchemaDto {

    @JsonProperty("uuid")
    private UUID uuid;

    @JsonProperty("type")
    private String type;

    @JsonProperty("title")
    private String title;

    @JsonProperty("defaultActionLabel")
    private String defaultActionLabel;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("fields")
    private Set<FieldDto> fields;

    public static SchemaDto from(Schema schema) {
        Set<FieldDto> fieldDtos = schema.getFields().stream().map(FieldDto::from).collect(Collectors.toSet());

        return new SchemaDto(schema.getUuid(), schema.getType(), schema.getTitle(), schema.getActionLabel(), schema.isActive(), fieldDtos);
    }
}
