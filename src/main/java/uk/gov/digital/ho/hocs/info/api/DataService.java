package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class DataService {

    public List<String> getCohorts() {
        List<String> cohorts = Arrays.asList(new String[]{"C pre 73","NC pre 73","C 01-01-73 to 31-07-88","NC 01-01-73 to 31-07-88","C children of","NC children of"});
        return cohorts;
    }
}
