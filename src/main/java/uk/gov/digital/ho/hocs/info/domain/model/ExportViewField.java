package uk.gov.digital.ho.hocs.info.domain.model;

import com.amazonaws.util.CollectionUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.api.dto.ExportViewFieldAdapterDto;
import uk.gov.digital.ho.hocs.info.api.dto.ExportViewFieldDto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@javax.persistence.Entity
@Table(name = "export_view_field")
public class ExportViewField implements Serializable {

    @Id
    @Column(name = "id", updatable = false, insertable = false)
    private Long id;

    @Column(name = "parent_export_view_id", updatable = false, insertable = false)
    private Long parentExportViewId;

    @Column(name = "sort_order", updatable = false, insertable = false)
    private Long sortOrder;

    @Column(name = "display_name", updatable = false, insertable = false)
    private String displayName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_export_view_field_id",
                referencedColumnName = "id",
                insertable = false,
                updatable = false)
    @OrderBy("sortOrder")
    private List<ExportViewFieldAdapter> adapters;

    public ExportViewFieldDto toDto() {
        List<ExportViewFieldAdapterDto> adapterDtos = CollectionUtils.isNullOrEmpty(adapters)
            ? new ArrayList<>()
            : adapters.stream().map(ExportViewFieldAdapter::toDto).collect(Collectors.toList());
        return new ExportViewFieldDto(id, parentExportViewId, sortOrder, displayName, adapterDtos);
    }

}
