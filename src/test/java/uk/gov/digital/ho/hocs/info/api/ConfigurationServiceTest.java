package uk.gov.digital.ho.hocs.info.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.domain.model.Configuration;
import uk.gov.digital.ho.hocs.info.domain.model.SearchField;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackColumn;
import uk.gov.digital.ho.hocs.info.domain.repository.ConfigurationRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;


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
    public void shouldReturnConfiguration() {
        String systemName = "system";
        String systemDisplayName = "Test System Name";
        String docLabelString = "label1,label2";
        List<WorkstackColumn> workstackColumns = Arrays.asList(new WorkstackColumn(10L, "system", "columnName1", "adapter", "renderer", "valueKey", true, "cssClass"));
        List<SearchField> searchFields = Arrays.asList(new SearchField(10L, "system", "name", "component", "validationRules", "properties"));

        when(configurationRepository.findBySystemName(systemName)).thenReturn(new Configuration(systemName, systemDisplayName, docLabelString, false, false, workstackColumns, searchFields, true));

        Configuration result = configurationService.getConfiguration(systemName);

        Assert.assertEquals("System name incorrect", systemName, result.getSystemName());
        Assert.assertEquals("Display name do not match", systemDisplayName, result.getDisplayName());
        Assert.assertEquals("Bulk Create Setting is incorrect", false, result.isBulkCreateEnabled());
        Assert.assertEquals("Deadlines Enabled Setting is incorrect", false, result.isDeadlinesEnabled());
        Assert.assertEquals("Document labels do not match", docLabelString, result.getDocumentLabels());
        Assert.assertEquals("AutoCreateAndAllocateEnabled do not match", true, result.isAutoCreateAndAllocateEnabled());
        Assert.assertEquals("There should be 1 workstack column", 1, result.getWorkstackColumns().size());
        Assert.assertEquals("Workstack column display name do not match", "columnName1", result.getWorkstackColumns().get(0).getDisplayName());
        Assert.assertEquals("Workstack column data adapter do not match", "adapter", result.getWorkstackColumns().get(0).getDataAdapter());
        Assert.assertEquals("Workstack column renderer do not match", "renderer", result.getWorkstackColumns().get(0).getRenderer());
        Assert.assertEquals("Workstack column data value key do not match", "valueKey", result.getWorkstackColumns().get(0).getDataValueKey());
        Assert.assertEquals("Workstack column isFilterable do not match", true, result.getWorkstackColumns().get(0).isFilterable());
        Assert.assertEquals("Workstack column parent system name do not match", "system", result.getWorkstackColumns().get(0).getParentSystemName());
        Assert.assertEquals("Workstack column header class name do not match", "cssClass", result.getWorkstackColumns().get(0).getHeaderClassName());
        Assert.assertEquals("There should be 1 search field", 1, result.getSearchFields().size());
        Assert.assertEquals("Search field name do not match", "name", result.getSearchFields().get(0).getName());
        Assert.assertEquals("Search field parent system name do not match", "system", result.getSearchFields().get(0).getParentSystemName());
        Assert.assertEquals("Search field component do not match", "component", result.getSearchFields().get(0).getComponent());
        Assert.assertEquals("Search field validation do not match", "validationRules", result.getSearchFields().get(0).getValidation());
        Assert.assertEquals("Search field props do not match", "properties", result.getSearchFields().get(0).getProps());


        verify(configurationRepository).findBySystemName(systemName);
        verifyNoMoreInteractions(configurationRepository);

    }

}