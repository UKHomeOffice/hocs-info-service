package uk.gov.digital.ho.hocs.info.deadline;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.dto.DeadlineDto;
import uk.gov.digital.ho.hocs.info.dto.GetDeadlinesRequest;
import uk.gov.digital.ho.hocs.info.dto.GetDeadlinesResponse;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
public class DeadlinesResource {

    private final DeadlinesService deadlinesService;

    @Autowired
    public DeadlinesResource(DeadlinesService deadlinesService) {
        this.deadlinesService = deadlinesService;
    }

    @RequestMapping(value = "/deadlines", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetDeadlinesResponse> getDeadlines(@RequestBody GetDeadlinesRequest getDeadlineRequest) {
        log.info("requesting deadlines for casetype {} with received date of {} ", getDeadlineRequest.getCaseType(), getDeadlineRequest.getDate());
        try {
            Set<DeadlineDto> deadlineDtos = deadlinesService.getDeadlines(getDeadlineRequest.getCaseType(), getDeadlineRequest.getDate());
            return ResponseEntity.ok(GetDeadlinesResponse.from(deadlineDtos));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/deadline", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeadlineDto> getDeadlinesForRequestedSLA(@RequestBody GetDeadlinesRequest getDeadlineRequest) {
        log.info("requesting deadline with received date of {} and SLA of {} days", getDeadlineRequest.getDate(), getDeadlineRequest.getSla());
        try {
            DeadlineDto deadlineDtos = deadlinesService.getDeadlinesForRequestedSLA(getDeadlineRequest.getCaseType(), getDeadlineRequest.getDate(), getDeadlineRequest.getSla());
            return ResponseEntity.ok(deadlineDtos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

