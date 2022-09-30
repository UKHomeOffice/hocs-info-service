package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.Configuration;
import uk.gov.digital.ho.hocs.info.domain.model.Profile;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackColumn;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ConfigurationDtoTest {

    @Test
    public void from() {
        String systemName = "system";
        String systemDisplayName = "Demo System";

        String columnDisplayName = "ColumnABC";
        String columnDataAdapter = "adapter123";
        String columnRenderer = "renderer";
        String columnDataValueKey = "valueKey";
        Boolean columnFilterable = true;
        String columnHeaderClassName = "some css header name";
        String columnSortStrategy = "TestSortStrategy";

        String profileName = "Profile1";

        String workstackType = "SomeType";

        List<WorkstackColumn> workstackColumns = Arrays.asList(
            new WorkstackColumn(UUID.randomUUID(), columnDisplayName, columnDataAdapter, columnRenderer,
                columnDataValueKey, columnFilterable, columnHeaderClassName, columnSortStrategy));

        List<WorkstackType> workstackTypes = Arrays.asList(
            new WorkstackType(10L, systemName, workstackType, workstackColumns));

        List<Profile> profiles = Arrays.asList(new Profile(profileName, systemName, false));

        String readOnlyCaseViewAdapter = "Adapter";
        Configuration configuration = new Configuration(systemName, systemDisplayName, true, true, true, workstackTypes,
            profiles, true, readOnlyCaseViewAdapter);

        ConfigurationDto dto = ConfigurationDto.from(configuration);

        Assert.assertEquals("Display name do not match", systemDisplayName, dto.getDisplayName());
        Assert.assertEquals("Bulk Create setting is incorrect", true, dto.isBulkCreateEnabled());
        Assert.assertEquals("Deadline Enabled setting is incorrect", true, dto.isDeadlinesEnabled());
        Assert.assertEquals("View Standard Lines setting is incorrect", true, dto.isViewStandardLinesEnabled());
        Assert.assertEquals("AutoCreateAndAllocateEnabled setting is incorrect", true,
            dto.isAutoCreateAndAllocateEnabled());
        Assert.assertEquals("ReadOnlyCaseViewAdapter setting is incorrect", readOnlyCaseViewAdapter,
            dto.getReadOnlyCaseViewAdapter());

        Assert.assertEquals("There should be 1 workstack column", 1, dto.getWorkstackTypeColumns().size());
        Assert.assertEquals("Workstack column display name do not match", columnDisplayName,
            dto.getWorkstackTypeColumns().get(0).getWorkstackColumns().get(0).getDisplayName());
        Assert.assertEquals("Workstack type matches expected value", workstackType,
            dto.getWorkstackTypeColumns().get(0).getType());
        Assert.assertEquals("Workstack column data adapter do not match", columnDataAdapter,
            dto.getWorkstackTypeColumns().get(0).getWorkstackColumns().get(0).getDataAdapter());
        Assert.assertEquals("Workstack column renderer do not match", columnRenderer,
            dto.getWorkstackTypeColumns().get(0).getWorkstackColumns().get(0).getRenderer());
        Assert.assertEquals("Workstack column data value key do not match", columnDataValueKey,
            dto.getWorkstackTypeColumns().get(0).getWorkstackColumns().get(0).getDataValueKey());
        Assert.assertEquals("Workstack column isFilterable do not match", columnFilterable,
            dto.getWorkstackTypeColumns().get(0).getWorkstackColumns().get(0).isFilterable());
        Assert.assertEquals("Workstack column header class name do not match", columnHeaderClassName,
            dto.getWorkstackTypeColumns().get(0).getWorkstackColumns().get(0).getHeaderClassName());
        Assert.assertEquals("Workstack column sort strategy do not match", columnSortStrategy,
            dto.getWorkstackTypeColumns().get(0).getWorkstackColumns().get(0).getSortStrategy());

        Assert.assertEquals("Profile name do not match", profileName, dto.getProfiles().get(0).getProfileName());
        Assert.assertEquals("Profile summaryDeadlinesEnabled name do not match", false,
            dto.getProfiles().get(0).isSummaryDeadlineEnabled());
    }

}
