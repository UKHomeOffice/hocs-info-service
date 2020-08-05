package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.Schema;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SchemaDto {

    @JsonProperty("uuid")
    private UUID uuid;

    @JsonProperty("stageType")
    private String stageType;

    @JsonProperty("type")
    private String type;

    @JsonProperty("title")
    private String title;

    @JsonProperty("defaultActionLabel")
    private String defaultActionLabel;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("fields")
    private List<FieldDto> fields;

    @JsonProperty("secondaryActions")
    private List<SecondaryActionDto> secondaryActions;

    @JsonRawValue
    private String props;

    public static SchemaDto from(Schema schema) {
        List<FieldDto> fieldDtos = schema.getFields().stream().map(FieldDto::from).collect(Collectors.toList());
        List<SecondaryActionDto> secondaryActionsDtos = schema.getSecondaryActions().stream().map(SecondaryActionDto::from).collect(Collectors.toList());

        return new SchemaDto(schema.getUuid(), schema.getStageType(), schema.getType(), schema.getTitle(), schema.getActionLabel(),
                schema.isActive(), fieldDtos, secondaryActionsDtos, schema.getProps());
    }
}
