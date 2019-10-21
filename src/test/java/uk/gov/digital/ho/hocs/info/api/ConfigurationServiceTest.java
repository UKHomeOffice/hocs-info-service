package uk.gov.digital.ho.hocs.info.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.domain.model.Configuration;
import uk.gov.digital.ho.hocs.info.domain.repository.ConfigurationRepository;
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
        String workstackColumnString = "column1,column2";

        when(configurationRepository.findBySystemName(systemName)).thenReturn(new Configuration(systemName, systemDisplayName, docLabelString, workstackColumnString));

        Configuration result = configurationService.getConfiguration(systemName);

        Assert.assertEquals("System name incorrect", systemName, result.getSystemName());
        Assert.assertEquals("Display name do not match", systemDisplayName, result.getDisplayName());
        Assert.assertEquals("Document labels do not match", docLabelString, result.getDocumentLabels());
        Assert.assertEquals("Workstack columns do not match", workstackColumnString, result.getWorkstackColumns());

        verify(configurationRepository).findBySystemName(systemName);
        verifyNoMoreInteractions(configurationRepository);

    }

}