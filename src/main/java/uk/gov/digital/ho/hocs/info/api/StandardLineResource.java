package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.CreateStandardLineDocumentDto;
import uk.gov.digital.ho.hocs.info.api.dto.GetStandardLineResponse;
import uk.gov.digital.ho.hocs.info.domain.model.StandardLine;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class StandardLineResource {

    private final StandardLineService standardLineService;

    @Autowired
    public StandardLineResource(StandardLineService standardLineService) {
        this.standardLineService = standardLineService;
    }

    @GetMapping(value = "/standardLine/case/{caseUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetStandardLineResponse> getStandardLinesForPrimaryTopic(@PathVariable UUID caseUUID) {
        StandardLine standardLines = standardLineService.getStandardLinesForCase(caseUUID);
        return ResponseEntity.ok(GetStandardLineResponse.from(standardLines));
    }

    @PostMapping(value = "/standardLine", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createDocument(@RequestBody CreateStandardLineDocumentDto request) {
        standardLineService.createStandardLine(request.getDisplayName(), request.getTopicUUID(), request.getExpires(), request.getS3UntrustedUrl());
        return ResponseEntity.ok().build();
    }
}