package uk.gov.digital.ho.hocs.info.standardLine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.dto.CreateStandardLineDocumentDto;
import uk.gov.digital.ho.hocs.info.dto.GetStandardLineResponse;
import uk.gov.digital.ho.hocs.info.entities.StandardLine;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class StandardLineResource {

    private final StandardLineService standardLineService;

    @Autowired
    public StandardLineResource(StandardLineService standardLineService) {
        this.standardLineService = standardLineService;
    }

    @GetMapping(value = "standardlines/{topicUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetStandardLineResponse> getStandardLinesForPrimaryTopic(@PathVariable UUID topicUUID) {
        StandardLine standardLines = standardLineService.getStandardLines(topicUUID);
        return ResponseEntity.ok(GetStandardLineResponse.from(standardLines));

    }

    @PostMapping(value = "standardline/document", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createDocument(@RequestBody CreateStandardLineDocumentDto request) {
        standardLineService.createStandardLineDocument(request);
        return ResponseEntity.ok().build();
    }
}