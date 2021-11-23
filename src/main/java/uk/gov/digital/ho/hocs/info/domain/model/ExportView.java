package uk.gov.digital.ho.hocs.info.domain.model;

import com.amazonaws.util.CollectionUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.api.dto.ExportViewDto;
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
@Table(name = "export_view")
public class ExportView implements Serializable {

    @Id
    @Column(name = "id", updatable = false, insertable = false)
    private Long id;

    @Column(name = "code", updatable = false, insertable = false)
    private String code;

    @Column(name = "display_name", updatable = false, insertable = false)
    private String displayName;

    @Column(name = "required_permission", updatable = false, insertable = false)
    private String requiredPermission;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_export_view_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OrderBy("sortOrder")
    private List<ExportViewField> fields;

    public ExportViewDto toDto() {
        List<ExportViewFieldDto> fieldDtos = CollectionUtils.isNullOrEmpty(fields) ? new ArrayList<>() : fields.stream().map(ExportViewField::toDto).collect(Collectors.toList());
        return new ExportViewDto(id, code, displayName, requiredPermission, fieldDtos);
    }
}
