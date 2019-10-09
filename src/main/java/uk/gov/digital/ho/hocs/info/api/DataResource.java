package uk.gov.digital.ho.hocs.info.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DataResource {
    private DataService dataService;

    public DataResource(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping(value = "/data/cohorts")
    public ResponseEntity<List<String>> getCohorts() {
        List<String> cohorts = dataService.getCohorts();
        return ResponseEntity.ok(cohorts);
    }
}
