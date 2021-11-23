package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class CaseTypeSchemaResource {

    private final CaseTypeSchemaService caseTypeSchemaService;

    @Autowired
    public CaseTypeSchemaResource(CaseTypeSchemaService caseTypeSchemaService) {
        this.caseTypeSchemaService = caseTypeSchemaService;
    }

    @GetMapping(value = "/caseType/{caseType}/stages", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<String>> getCaseTypeStages(@PathVariable String caseType) {
        return ResponseEntity.ok(caseTypeSchemaService.getCaseTypeStages(caseType));
    }

}
