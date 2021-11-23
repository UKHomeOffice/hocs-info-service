package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackColumn;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class WorkstackColumnDto {


    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("dataAdapter")
    private String dataAdapter;

    @JsonProperty("renderer")
    private String renderer;

    @JsonProperty("dataValueKey")
    private String dataValueKey;

    @JsonProperty("isFilterable")
    private boolean filterable;

    @JsonProperty("headerClassName")
    private String headerClassName;

    @JsonProperty("sortStrategy")
    private String sortStrategy;

    public static WorkstackColumnDto from(WorkstackColumn column) {
        return new WorkstackColumnDto(column.getDisplayName(), column.getDataAdapter(), column.getRenderer(),
                column.getDataValueKey(), column.isFilterable(), column.getHeaderClassName(), column.getSortStrategy());
    }
}
