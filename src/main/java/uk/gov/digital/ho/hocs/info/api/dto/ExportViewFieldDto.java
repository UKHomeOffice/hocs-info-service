package uk.gov.digital.ho.hocs.info.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@java.lang.SuppressWarnings("squid:S1068")
@Getter
@AllArgsConstructor
public class ExportViewFieldDto {

    private Long id;
    private Long parentExportViewId;
    private Long sortOrder;
    private String displayName;
    private List<ExportViewFieldAdapterDto> adapters;
}
