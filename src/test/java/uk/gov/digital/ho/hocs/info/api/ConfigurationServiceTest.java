package uk.gov.digital.ho.hocs.info.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.domain.model.Configuration;
import uk.gov.digital.ho.hocs.info.domain.model.Profile;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackColumn;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackType;
import uk.gov.digital.ho.hocs.info.domain.repository.ConfigurationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationServiceTest {

    @Mock
    private ConfigurationRepository configurationRepository;

    private ConfigurationService configurationService;

    @Before
    public void setUp() {
        this.configurationService = new ConfigurationService(configurationRepository);
    }

    @Test
    public void shouldReturnConfiguration() throws Exception {
        String systemName = "profile";
        String systemDisplayName = "Test System Name";
        List<WorkstackColumn> workstackColumns = Arrays.asList(
            new WorkstackColumn(UUID.randomUUID(), "columnName1", "adapter", "renderer", "valueKey", true, "cssClass",
                "SortStrategy"));
        List<WorkstackType> workstackTypes = Arrays.asList(
            new WorkstackType(10L, systemName, "some_type", workstackColumns));
        List<Profile> profiles = Arrays.asList(new Profile("testProfile", "system", true));
        String readOnlyCaseViewAdapter = "Adapter";
        when(configurationRepository.findBySystemName(systemName)).thenReturn(
            new Configuration(systemName, systemDisplayName, false, false, false, workstackTypes, profiles, true,
                readOnlyCaseViewAdapter));

        Configuration result = configurationService.getConfiguration(systemName);

        Assert.assertEquals("System name incorrect", systemName, result.getSystemName());
        Assert.assertEquals("Display name do not match", systemDisplayName, result.getDisplayName());
        Assert.assertEquals("Bulk Create Setting is incorrect", false, result.isBulkCreateEnabled());
        Assert.assertEquals("View Standard Lines setting is incorrect", false, result.isViewStandardLinesEnabled());
        Assert.assertEquals("Deadlines Enabled Setting is incorrect", false, result.isDeadlinesEnabled());
        Assert.assertEquals("AutoCreateAndAllocateEnabled do not match", true, result.isAutoCreateAndAllocateEnabled());
        Assert.assertEquals("ReadOnlyCaseViewAdapter do not match", readOnlyCaseViewAdapter,
            result.getReadOnlyCaseViewAdapter());
        Assert.assertEquals("There should be 1 workstack column", 1, result.getWorkstackTypes().size());
        Assert.assertEquals("Workstack column display name do not match", "columnName1",
            result.getWorkstackTypes().get(0).getWorkstackColumns().get(0).getDisplayName());
        Assert.assertEquals("Workstack column data adapter do not match", "adapter",
            result.getWorkstackTypes().get(0).getWorkstackColumns().get(0).getDataAdapter());
        Assert.assertEquals("Workstack column renderer do not match", "renderer",
            result.getWorkstackTypes().get(0).getWorkstackColumns().get(0).getRenderer());
        Assert.assertEquals("Workstack column data value key do not match", "valueKey",
            result.getWorkstackTypes().get(0).getWorkstackColumns().get(0).getDataValueKey());
        Assert.assertEquals("Workstack column isFilterable do not match", true,
            result.getWorkstackTypes().get(0).getWorkstackColumns().get(0).isFilterable());
        Assert.assertEquals("Workstack column header class name do not match", "cssClass",
            result.getWorkstackTypes().get(0).getWorkstackColumns().get(0).getHeaderClassName());
        Assert.assertEquals("Workstack column sort strategy do not match", "SortStrategy",
            result.getWorkstackTypes().get(0).getWorkstackColumns().get(0).getSortStrategy());
        Assert.assertEquals("Workstack type matches expected value", "some_type",
            result.getWorkstackTypes().get(0).getType());
        Assert.assertEquals("There should be 1 profile", 1, result.getProfiles().size());
        Assert.assertEquals("Profile name do not match", "testProfile", result.getProfiles().get(0).getProfileName());
        Assert.assertEquals("Profile summaryDeadlinesEnabled do not match", true,
            result.getProfiles().get(0).isSummaryDeadlinesEnabled());
        Assert.assertEquals("Profile parent system name do not match", "system",
            result.getProfiles().get(0).getParentSystemName());

        verify(configurationRepository).findBySystemName(systemName);
        verifyNoMoreInteractions(configurationRepository);

    }

}
