package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.CreateStandardLineDocumentDto;
import uk.gov.digital.ho.hocs.info.api.dto.GetStandardLineResponse;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateStandardLineDto;
import uk.gov.digital.ho.hocs.info.domain.model.StandardLine;

import java.util.List;
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

    @GetMapping(value = "/standardLine/{standardLineUuid}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetStandardLineResponse> getStandardLine(@PathVariable UUID standardLineUuid) {
        StandardLine standardLine = standardLineService.getStandardLine(standardLineUuid);

        if (standardLine != null) {
            return ResponseEntity.ok(GetStandardLineResponse.from(standardLine));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/topic/{topicUUID}/standardLine", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetStandardLineResponse> getStandardLinesForPrimaryTopic(@PathVariable UUID topicUUID) {
        StandardLine standardLine = standardLineService.getStandardLineForTopic(topicUUID);
        return ResponseEntity.ok(GetStandardLineResponse.from(standardLine));
    }

    @GetMapping(value = "/user/{userUUID}/standardLine", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<GetStandardLineResponse>> getStandardLinesForUser(@PathVariable UUID userUUID) {
        List<StandardLine> standardLines = standardLineService.getStandardLinesForUser(userUUID);
        return ResponseEntity.ok(standardLines.stream().map(GetStandardLineResponse::from).collect(Collectors.toList()));
    }

    @GetMapping(value = "/standardLine/all", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<GetStandardLineResponse>> getAllStandardLines() {
        List<StandardLine> standardLines = standardLineService.getAllStandardLines();
        return ResponseEntity.ok(standardLines.stream().map(GetStandardLineResponse::from).collect(Collectors.toList()));
    }

    @PostMapping(value = "/standardLine", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createStandardLine(@RequestBody CreateStandardLineDocumentDto request) {
        standardLineService.createStandardLine(request.getDisplayName(), request.getTopicUUID(), request.getExpires(), request.getS3UntrustedUrl());
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/standardLine/{standardLineUuid}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity updateStandardLine(@PathVariable UUID standardLineUuid, @RequestBody UpdateStandardLineDto request) {
        standardLineService.updateStandardLine(standardLineUuid, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/standardLine/expire/{standardLineUuid}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity expireStandardLine(@PathVariable UUID standardLineUuid) {
        standardLineService.expireStandardLine(standardLineUuid);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/standardLine/delete/{standardLineUuid}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity deleteStandardLine(@PathVariable UUID standardLineUuid) {
        standardLineService.deleteStandardLine(standardLineUuid);
        return ResponseEntity.ok().build();
    }
}
