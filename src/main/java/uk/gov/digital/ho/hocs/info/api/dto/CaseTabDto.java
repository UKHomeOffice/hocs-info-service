package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.domain.model.CaseConfig;
import uk.gov.digital.ho.hocs.info.domain.model.CaseTab;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CaseTabDto {

    @JsonProperty("name")
    String name;

    @JsonProperty("label")
    String label;

    @JsonProperty("screen")
    String screen;

    public static CaseTabDto from(CaseTab caseTab) {
        return new CaseTabDto(caseTab.getName(), caseTab.getLabel(), caseTab.getScreen());
    }

}
