package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.domain.model.CaseConfig;
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CaseConfigDto {

    @JsonProperty("type")
    String type;

    @JsonProperty("tabs")
    List<String> tabs;

    public static CaseConfigDto from(CaseConfig caseConfig) {
        return new CaseConfigDto(caseConfig.getType(), caseConfig.getTabs());
    }

}
