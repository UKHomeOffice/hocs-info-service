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

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ConfigurationDto {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("documentLabels")
    private List<String> documentLabels;

    @JsonProperty("workstackColumns")
    private List<String> workstackColumns;


    public static ConfigurationDto from(Configuration configuration) {
        String documentLabelsString = configuration.getDocumentLabels();
        List<String> documentLabels = StringUtils.isBlank(documentLabelsString) ? null : new ArrayList<>(Arrays.asList(documentLabelsString.split(",")));

        String workstackColumnsString = configuration.getWorkstackColumns();
        List<String> workstackColumns = StringUtils.isBlank(workstackColumnsString) ? null : new ArrayList<>(Arrays.asList(workstackColumnsString.split(",")));

        return new ConfigurationDto(
                configuration.getDisplayName(),
                documentLabels,
                workstackColumns);
    }

}
