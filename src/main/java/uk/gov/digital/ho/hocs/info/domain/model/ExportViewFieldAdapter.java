package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.api.dto.ExportViewFieldAdapterDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "export_view_field_adapter")
public class ExportViewFieldAdapter implements Serializable {

    @Id
    @Column(name = "id", updatable = false, insertable = false)
    private Long id;

    @Column(name = "parent_export_view_field_id", updatable = false, insertable = false)
    private Long parentExportViewFieldId;

    @Column(name = "sort_order", updatable = false, insertable = false)
    private Long sortOrder;

    @Column(name = "type", updatable = false, insertable = false)
    private String type;


    public ExportViewFieldAdapterDto toDto() {
        return new ExportViewFieldAdapterDto(id, parentExportViewFieldId, sortOrder, type);
    }

}
