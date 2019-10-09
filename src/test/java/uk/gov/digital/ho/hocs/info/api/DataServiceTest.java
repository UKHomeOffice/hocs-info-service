package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DataServiceTest {

    private DataService dataService;

    @Before
    public void setUp() {
        dataService = new DataService();
    }

    @Test
    public void shouldGetCohorts(){
        List<String> cohorts = dataService.getCohorts();

        assertThat(cohorts).isEqualTo(Arrays.asList(new String[]{"C pre 73","NC pre 73","C 01-01-73 to 31-07-88","NC 01-01-73 to 31-07-88","C children of","NC children of"}));
    }
}
