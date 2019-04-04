package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.CreateStandardLineDocumentDto;
import uk.gov.digital.ho.hocs.info.api.dto.GetStandardLineResponse;
import uk.gov.digital.ho.hocs.info.domain.model.StandardLine;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class StandardLineResource {

    private final StandardLineService standardLineService;

    @Autowired
    public StandardLineResource(StandardLineService standardLineService) {
        this.standardLineService = standardLineService;
    }

    @GetMapping(value = "/standardLine", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<GetStandardLineResponse>> getStandardLines() {
        Set<StandardLine> standardLines = standardLineService.getActiveStandardLines();
        return ResponseEntity.ok(standardLines.stream().map(GetStandardLineResponse::from).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/topic/{topicUUID}/standardLine", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetStandardLineResponse> getStandardLinesForPrimaryTopic(@PathVariable UUID topicUUID) {
        StandardLine standardLine = standardLineService.getStandardLineForTopic(topicUUID);
        return ResponseEntity.ok(GetStandardLineResponse.from(standardLine));
    }

    @PostMapping(value = "/standardLine", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createDocument(@RequestBody CreateStandardLineDocumentDto request) {
        standardLineService.createStandardLine(request.getDisplayName(), request.getTopicUUID(), request.getExpires(), request.getS3UntrustedUrl());
        return ResponseEntity.ok().build();
    }
}