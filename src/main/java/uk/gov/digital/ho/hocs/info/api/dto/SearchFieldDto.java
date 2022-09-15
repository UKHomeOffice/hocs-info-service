package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.SearchField;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SearchFieldDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("component")
    private String component;

    @JsonRawValue
    private String validation;

    @JsonRawValue
    private String props;

    public static SearchFieldDto from(SearchField field) {
        return new SearchFieldDto(field.getName(), field.getComponent(), field.getValidation(), field.getProps());
    }

}
