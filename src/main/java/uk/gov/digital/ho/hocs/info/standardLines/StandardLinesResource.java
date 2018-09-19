package uk.gov.digital.ho.hocs.info.standardLines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.dto.GetStandardLinesResponse;
import uk.gov.digital.ho.hocs.info.entities.StandardLines;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class StandardLinesResource {

    private final StandardLinesService standardLinesService;

    @Autowired
    public StandardLinesResource(StandardLinesService standardLinesService) {
        this.standardLinesService = standardLinesService;
    }

    @GetMapping(value = "/casetype/{caseType}/standardline/{topicUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetStandardLinesResponse> getTemplateForCaseTypes(@PathVariable String caseType, @PathVariable UUID topicUUID) {
        try {
            List<StandardLines> standardLines = standardLinesService.getStandardLines(caseType, topicUUID);
            return ResponseEntity.ok(GetStandardLinesResponse.from(standardLines));
        } catch (EntityPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}