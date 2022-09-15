package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.SecondaryAction;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SecondaryActionDto {

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

    public static SecondaryActionDto from(SecondaryAction secondaryAction) {
        return new SecondaryActionDto(secondaryAction.getUuid(), secondaryAction.getComponent(),
            secondaryAction.getValidation(), secondaryAction.getName(), secondaryAction.getLabel(),
            secondaryAction.getProps());
    }

}
