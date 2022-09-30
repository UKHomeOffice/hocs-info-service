package uk.gov.digital.ho.hocs.info.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.ConfigurationDto;
import uk.gov.digital.ho.hocs.info.domain.model.Configuration;
import uk.gov.digital.ho.hocs.info.domain.model.Profile;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackColumn;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationResourceTest {

    @Mock
    private ConfigurationService configurationService;

    private ConfigurationResource configurationResource;

    @Before
    public void setUp() {
        configurationResource = new ConfigurationResource(configurationService);
    }

    @Test
    public void shouldReturnConfiguration() throws Exception {
        String systemName = "system";
        String systemDisplayName = "Test System Name";

        List<WorkstackColumn> workstackColumns = Arrays.asList(
            new WorkstackColumn(UUID.randomUUID(), "columnName1", "adapter", "renderer", "valueKey", true, "cssClass",
                "SortStrategy"));
        List<WorkstackType> workstackTypes = Arrays.asList(
            new WorkstackType(10L, systemName, "some_type", workstackColumns));

        List<Profile> profiles = Arrays.asList(new Profile("testProfile", "system", true));

        String readOnlyCaseViewAdapter = "Adapter";
        when(configurationService.getConfiguration("system")).thenReturn(
            new Configuration(systemName, systemDisplayName, false, false, true, workstackTypes, profiles, false,
                readOnlyCaseViewAdapter));

        ResponseEntity<ConfigurationDto> result = configurationResource.getConfiguration();

        Assert.assertEquals("Status code incorrect", 200, result.getStatusCode().value());
        Assert.assertEquals("Display name do not match", systemDisplayName, result.getBody().getDisplayName());
        Assert.assertEquals("Bulk Create setting is incorrect", false, result.getBody().isBulkCreateEnabled());
        Assert.assertEquals("View Standard Lines setting is incorrect", false,
            result.getBody().isViewStandardLinesEnabled());
        Assert.assertEquals("Deadlines Enabled setting is incorrect", true, result.getBody().isDeadlinesEnabled());
        Assert.assertEquals("AutoCreateAndAllocateEnabled do not match", false,
            result.getBody().isAutoCreateAndAllocateEnabled());
        Assert.assertEquals("ReadOnlyCaseViewAdapter do not match", readOnlyCaseViewAdapter,
            result.getBody().getReadOnlyCaseViewAdapter());
        Assert.assertEquals("There should be 1 workstack column", 1, result.getBody().getWorkstackTypeColumns().size());
        Assert.assertEquals("Workstack column display name do not match", "columnName1",
            result.getBody().getWorkstackTypeColumns().get(0).getWorkstackColumns().get(0).getDisplayName());
        Assert.assertEquals("Workstack type matches expected", "some_type",
            result.getBody().getWorkstackTypeColumns().get(0).getType());
        Assert.assertEquals("Workstack column data adapter do not match", "adapter",
            result.getBody().getWorkstackTypeColumns().get(0).getWorkstackColumns().get(0).getDataAdapter());
        Assert.assertEquals("Workstack column renderer do not match", "renderer",
            result.getBody().getWorkstackTypeColumns().get(0).getWorkstackColumns().get(0).getRenderer());
        Assert.assertEquals("Workstack column data value key do not match", "valueKey",
            result.getBody().getWorkstackTypeColumns().get(0).getWorkstackColumns().get(0).getDataValueKey());
        Assert.assertEquals("Workstack column isFilterable do not match", true,
            result.getBody().getWorkstackTypeColumns().get(0).getWorkstackColumns().get(0).isFilterable());
        Assert.assertEquals("Workstack column header class name do not match", "cssClass",
            result.getBody().getWorkstackTypeColumns().get(0).getWorkstackColumns().get(0).getHeaderClassName());
        Assert.assertEquals("Workstack column sort strategy do not match", "SortStrategy",
            result.getBody().getWorkstackTypeColumns().get(0).getWorkstackColumns().get(0).getSortStrategy());
        Assert.assertEquals("Profile name do not match", "testProfile",
            result.getBody().getProfiles().get(0).getProfileName());
        Assert.assertEquals("Profile summaryDeadlinesEnabled name do not match", true,
            result.getBody().getProfiles().get(0).isSummaryDeadlineEnabled());

        verify(configurationService).getConfiguration(systemName);
        verifyNoMoreInteractions(configurationService);
    }

    @Test
    public void shouldReturnNotFoundOnException() throws Exception {
        when(configurationService.getConfiguration("system")).thenThrow(new RuntimeException());

        ResponseEntity<ConfigurationDto> result = configurationResource.getConfiguration();

        Assert.assertEquals("Status code incorrect", 500, result.getStatusCode().value());
    }

}
