package uk.gov.digital.ho.hocs.info.standardLine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.dto.GetStandardLineKeyResponse;
import uk.gov.digital.ho.hocs.info.dto.GetStandardLineResponse;
import uk.gov.digital.ho.hocs.info.entities.StandardLine;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class StandardLineResource {

    private final StandardLineService standardLineService;

    @Autowired
    public StandardLineResource(StandardLineService standardLineService) {
        this.standardLineService = standardLineService;
    }

    @GetMapping(value = "/casetype/{caseType}/standardlines/{topicUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetStandardLineResponse> getTemplateForCaseTypes(@PathVariable String caseType, @PathVariable UUID topicUUID) {
        try {
            List<StandardLine> standardLines = standardLineService.getStandardLines(caseType, topicUUID);
            return ResponseEntity.ok(GetStandardLineResponse.from(standardLines));
        } catch (EntityPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping(value = "/casetype/{caseType}/standardlinekey/{standardLineUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetStandardLineKeyResponse> getStandardLineKey(@PathVariable String caseType, @PathVariable UUID standardLineUUID) {
        StandardLine standardLine = standardLineService.getStandardLineKey(standardLineUUID);
        return ResponseEntity.ok(GetStandardLineKeyResponse.from(standardLine));
    }
}