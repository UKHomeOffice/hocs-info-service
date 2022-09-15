package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.CaseDetailsField;

@AllArgsConstructor()
@Getter
public class CaseDetailsFieldDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("component")
    private String component;

    @JsonRawValue
    private String props;

    public static CaseDetailsFieldDto from(CaseDetailsField field) {
        return new CaseDetailsFieldDto(field.getName(), field.getComponent(), field.getProps());
    }

}
