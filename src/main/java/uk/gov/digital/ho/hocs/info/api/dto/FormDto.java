package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.Form;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class FormDto {

    @JsonProperty("uuid")
    private UUID uuid;

    @JsonProperty("type")
    private String type;

    @JsonRawValue
    private String data;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("fields")
    private Set<FieldDto> fields;

    public static FormDto from(Form form) {
        Set<FieldDto> fieldDtos = form.getFields().stream().map(FieldDto::from).collect(Collectors.toSet());

        return new FormDto(form.getUuid(), form.getType(), form.getData(), form.isActive(), fieldDtos);
    }
}
