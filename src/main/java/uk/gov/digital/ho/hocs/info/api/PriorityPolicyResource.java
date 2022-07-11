package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.api.dto.PriorityPolicyDto;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Deprecated(forRemoval = true)
public class PriorityPolicyResource {

    private final PriorityPolicyService priorityPolicyService;

    @Autowired
    public PriorityPolicyResource(PriorityPolicyService priorityPolicyService) {
        this.priorityPolicyService = priorityPolicyService;
    }

    @Deprecated(forRemoval = true)
    @GetMapping(value = "/priority/policy/{caseType}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<PriorityPolicyDto>> getByCaseType(@PathVariable String caseType) {
        return ResponseEntity.ok(priorityPolicyService.getByCaseType(caseType));
    }

}
