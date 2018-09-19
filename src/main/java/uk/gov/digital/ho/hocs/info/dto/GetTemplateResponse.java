package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.entities.Template;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetTemplateResponse {

    @JsonProperty("templates")
    List<TemplateDto> templateDtos;

    public static GetTemplateResponse from(List<Template> templates) {
        List<TemplateDto> templateDtos = templates.stream().map(TemplateDto::from).collect(Collectors.toList());
        return new GetTemplateResponse(templateDtos);
    }
}
