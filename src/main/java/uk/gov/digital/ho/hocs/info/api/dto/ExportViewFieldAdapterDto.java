package uk.gov.digital.ho.hocs.info.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Deprecated(forRemoval = true)
public class ExportViewFieldAdapterDto {

    private Long id;
    private Long parentExportViewFieldId;
    private Long sortOrder;
    private String type;
}
