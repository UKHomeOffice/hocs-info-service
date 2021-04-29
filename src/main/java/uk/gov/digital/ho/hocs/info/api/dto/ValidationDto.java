package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.domain.model.ValidationRule;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ValidationDto {

    @JsonProperty("uuid")
    private UUID uuid;

    @JsonProperty("title")
    private String title;

    @JsonRawValue
    private String subSchema;

    public static ValidationDto from(ValidationRule validationRule) {
        return new ValidationDto(validationRule.getUuid(), validationRule.getTitle(), validationRule.getSubSchema());
    }

}
