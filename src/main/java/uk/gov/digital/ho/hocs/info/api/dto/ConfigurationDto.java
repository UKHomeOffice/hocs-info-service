package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.Configuration;
import uk.gov.digital.ho.hocs.info.domain.model.Profile;


import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ConfigurationDto {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("bulkCreateEnabled")
    private boolean bulkCreateEnabled;

    @JsonProperty("viewStandardLinesEnabled")
    private boolean viewStandardLinesEnabled;

    @JsonProperty("deadlinesEnabled")
    private boolean deadlinesEnabled;

    @JsonProperty("workstackTypeColumns")
    private List<WorkstackTypeDto> workstackTypeColumns;

    @JsonProperty("profiles")
    private List<ProfileDto> profiles;

    @JsonProperty("autoCreateAndAllocateEnabled")
    private boolean autoCreateAndAllocateEnabled;

    @JsonProperty("readOnlyCaseViewAdapter")
    private String readOnlyCaseViewAdapter;

    public static ConfigurationDto from(Configuration configuration) {
        List<WorkstackTypeDto> workstackTypeColumns = configuration.getWorkstackTypes().stream().map(WorkstackTypeDto::from).collect(Collectors.toList());
        List<ProfileDto> profileDtos = configuration.getProfiles().stream().map(ProfileDto::from).collect(Collectors.toList());

        return new ConfigurationDto(
                configuration.getDisplayName(),
                configuration.isBulkCreateEnabled(),
                configuration.isViewStandardLinesEnabled(),
                configuration.isDeadlinesEnabled(),
                workstackTypeColumns,
                profileDtos,
                configuration.isAutoCreateAndAllocateEnabled(),
                configuration.getReadOnlyCaseViewAdapter());
    }

}
