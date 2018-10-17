package uk.gov.digital.ho.hocs.info.standardLine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.dto.GetStandardLineResponse;
import uk.gov.digital.ho.hocs.info.entities.StandardLine;

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

    @GetMapping(value = "standardlines/{topicUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetStandardLineResponse> getStandardLinesForPrimaryTopic(@PathVariable UUID topicUUID) {
            List<StandardLine> standardLines = standardLineService.getStandardLines(topicUUID);
            return ResponseEntity.ok(GetStandardLineResponse.from(standardLines));
    }
}