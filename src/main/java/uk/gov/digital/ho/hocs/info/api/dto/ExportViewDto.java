package uk.gov.digital.ho.hocs.info.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Deprecated(forRemoval = true)
public class ExportViewDto {

    private Long id;

    private String code;

    private String displayName;

    private String requiredPermission;

    private List<ExportViewFieldDto> fields;

}
