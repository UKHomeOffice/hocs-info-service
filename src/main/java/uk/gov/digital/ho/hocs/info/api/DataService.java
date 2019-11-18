package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class DataService {

    public List<String> getCohorts() {
        List<String> cohorts = Arrays.asList(new String[]
                {
                        "Commonwealth pre 1973",
                        "Non-Commonwealth pre 1973",
                        "Commonwealth 1973 to July 1988",
                        "Non-Commonwealth 1973 to July 1988",
                        "Children of Commonwealth",
                        "Children of Non-Commonwealth"
                });
        return cohorts;
    }
}
