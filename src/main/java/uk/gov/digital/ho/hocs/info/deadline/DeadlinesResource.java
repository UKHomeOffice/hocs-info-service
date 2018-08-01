package uk.gov.digital.ho.hocs.info.deadline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.dto.GetDeadlinesResponse;
import uk.gov.digital.ho.hocs.info.entities.Deadline;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class DeadlinesResource {

    private final DeadlinesService deadlinesService;

    @Autowired
    public DeadlinesResource(DeadlinesService deadlinesService) {
        this.deadlinesService = deadlinesService;
    }

    @RequestMapping(value = "/casetype/{caseType}/deadlines/{received}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetDeadlinesResponse> getDeadlines(@PathVariable String caseType, @PathVariable String received) {
        try {
            LocalDate receivedDate = LocalDate.parse(received);
            Set<Deadline> deadlineDtos = deadlinesService.getDeadlines(caseType, receivedDate);
            return ResponseEntity.ok(GetDeadlinesResponse.from(deadlineDtos));
        } catch (EntityNotFoundException  e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch ( EntityPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build() ;
        }
    }
}

