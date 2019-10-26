package uk.gov.digital.ho.hocs.info.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.ConfigurationDto;
import uk.gov.digital.ho.hocs.info.api.dto.SearchFieldDto;
import uk.gov.digital.ho.hocs.info.domain.model.Configuration;
import uk.gov.digital.ho.hocs.info.domain.model.SearchField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

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
    public void shouldReturnConfiguration() {
        String systemName = "system";
        String systemDisplayName = "Test System Name";
        String docLabelString = "label1,label2";
        String workstackColumnString = "column1,column2";

        List<SearchField> searchFields = Arrays.asList(new SearchField(10L, "system", "name", "component", "validationRules", "properties"));

        when(configurationService.getConfiguration("system")).thenReturn(new Configuration(systemName, systemDisplayName, docLabelString, workstackColumnString, searchFields));

        ResponseEntity<ConfigurationDto> result = configurationResource.getConfiguration();


        Assert.assertEquals("Status code incorrect", 200, result.getStatusCode().value());
        Assert.assertEquals("Display name do not match", systemDisplayName, result.getBody().getDisplayName());
        Assert.assertEquals("Document labels do not match", new ArrayList<>(Arrays.asList(docLabelString.split(","))), result.getBody().getDocumentLabels());
        Assert.assertEquals("Workstack columns do not match", new ArrayList<>(Arrays.asList(workstackColumnString.split(","))), result.getBody().getWorkstackColumns());
        Assert.assertEquals("There should be 1 search field", 1, result.getBody().getSearchFields().size());
        Assert.assertEquals("Search field name do not match", "name", result.getBody().getSearchFields().get(0).getName());
        Assert.assertEquals("Search field component do not match", "component", result.getBody().getSearchFields().get(0).getComponent());
        Assert.assertEquals("Search field validation do not match", "validationRules", result.getBody().getSearchFields().get(0).getValidation());
        Assert.assertEquals("Search field props do not match", "properties", result.getBody().getSearchFields().get(0).getProps());

        verify(configurationService).getConfiguration(systemName);
        verifyNoMoreInteractions(configurationService);
    }

}