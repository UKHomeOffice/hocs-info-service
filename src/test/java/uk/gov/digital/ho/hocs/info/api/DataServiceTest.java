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

        assertThat(cohorts).isEqualTo(Arrays.asList(new String[]
                {
                        "Commonwealth pre 1973",
                        "Non-Commonwealth pre 1973",
                        "Commonwealth 1973 to July 1988",
                        "Non-Commonwealth 1973 to July 1988",
                        "Children of Commonwealth",
                        "Children of Non-Commonwealth"
                }));
    }
}
