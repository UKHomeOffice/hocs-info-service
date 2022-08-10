package uk.gov.digital.ho.hocs.info.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.domain.model.CaseConfig;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Deprecated(forRemoval = true)
public class CaseConfigDto {

    String type;

    List<CaseTabDto> tabs;

    public static CaseConfigDto from(CaseConfig caseConfig) {
        List<CaseTabDto> caseTabDtos = caseConfig.getTabs().stream().map(CaseTabDto::from).collect(Collectors.toList());
        return new CaseConfigDto(caseConfig.getType(), caseTabDtos);
    }

}
