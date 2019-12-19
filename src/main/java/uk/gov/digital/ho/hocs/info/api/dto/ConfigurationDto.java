package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ConfigurationDto {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("documentLabels")
    private List<String> documentLabels;

    @JsonProperty("bulkCreateEnabled")
    private boolean bulkCreateEnabled;

    @JsonProperty("deadlinesEnabled")
    private boolean deadlinesEnabled;

    @JsonProperty("workstackColumns")
    private List<WorkstackColumnDto> workstackColumns;

    @JsonProperty("searchFields")
    private List<SearchFieldDto> searchFields;

    @JsonProperty("autoCreateAndAllocateEnabled")
    private boolean autoCreateAndAllocateEnabled;

    @JsonProperty("readOnlyCaseViewAdapter")
    private String readOnlyCaseViewAdapter;

    public static ConfigurationDto from(Configuration configuration) {
        String documentLabelsString = configuration.getDocumentLabels();
        List<String> documentLabels = StringUtils.isBlank(documentLabelsString) ? null : new ArrayList<>(Arrays.asList(documentLabelsString.split(",")));

        List<WorkstackColumnDto> workstackColumns = configuration.getWorkstackColumns().stream().map(WorkstackColumnDto::from).collect(Collectors.toList());
        List<SearchFieldDto> searchFieldDtos = configuration.getSearchFields().stream().map(SearchFieldDto::from).collect(Collectors.toList());

        return new ConfigurationDto(
                configuration.getDisplayName(),
                documentLabels,
                configuration.isBulkCreateEnabled(),
                configuration.isDeadlinesEnabled(),
                workstackColumns,
                searchFieldDtos,
                configuration.isAutoCreateAndAllocateEnabled(),
                configuration.getReadOnlyCaseViewAdapter());
    }

}
