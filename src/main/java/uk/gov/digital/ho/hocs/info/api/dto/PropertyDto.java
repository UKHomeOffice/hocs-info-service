package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PropertyDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("label")
    private String label;

    @JsonRawValue
    private String choices;

    public static PropertyDto from(String name, String label, String choices) {
        return new PropertyDto(name, label, choices);
    }
}
