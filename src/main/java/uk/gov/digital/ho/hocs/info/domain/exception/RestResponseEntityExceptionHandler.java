package uk.gov.digital.ho.hocs.info.domain.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uk.gov.digital.ho.hocs.info.security.KeycloakException;
import uk.gov.digital.ho.hocs.info.api.dto.TeamDeleteActiveParentTopicsDto;

import static net.logstash.logback.argument.StructuredArguments.value;
import static org.springframework.http.HttpStatus.*;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.*;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationExceptions.EntityCreationException.class)
    public ResponseEntity handle(ApplicationExceptions.EntityCreationException e) {
        log.error("ApplicationExceptions.EntityCreationException: {}", e.getMessage(),value(EVENT, e.getEvent()));
        return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApplicationExceptions.EntityNotFoundException.class)
    public ResponseEntity handle(ApplicationExceptions.EntityNotFoundException e) {
        log.error("ApplicationExceptions.EntityNotFoundException: {}", e.getMessage(),value(EVENT, e.getEvent()));
        return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(ApplicationExceptions.ResourceServerException.class)
    public ResponseEntity handle(ApplicationExceptions.ResourceServerException e) {
        log.error("ApplicationExceptions.ResourceServerException: {}", e.getMessage(),value(EVENT, e.getEvent()));
        return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApplicationExceptions.ResourceNotFoundException.class)
    public ResponseEntity handle(ApplicationExceptions.ResourceNotFoundException e) {
        log.error("ApplicationExceptions.ResourceNotFoundException: {}", e.getMessage(),value(EVENT, e.getEvent()));
        return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handle(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: {}", e.getMessage(),value(EVENT, BAD_REQUEST));
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity handle(HttpMessageConversionException e) {
        log.error("HttpMessageConversionException: {}", e.getMessage(),value(EVENT, BAD_REQUEST), value(EXCEPTION, e));
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handle(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException: {}", e.getMessage(),value(EVENT, BAD_REQUEST), value(EXCEPTION, e));
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationExceptions.CorrespondentCreationException.class)
    public ResponseEntity handle(ApplicationExceptions.CorrespondentCreationException e) {
        log.error("DataIntegrityViolationException: {}", e.getMessage(),value(EVENT, BAD_REQUEST), value(EXCEPTION, e));
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(KeycloakException.class)
    public ResponseEntity handle(KeycloakException e) {
        log.error("Keycloak exception: {}", e.getMessage(),e.getMessage(),value(EVENT, KEYCLOAK_FAILURE));
        return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApplicationExceptions.ConstituencyCreationException.class)
    public ResponseEntity handle(ApplicationExceptions.ConstituencyCreationException e) {
        log.error("ConstituencyCreationException: {}", e.getMessage(), value(EVENT, BAD_REQUEST));
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationExceptions.TeamDeleteException.class)
    public ResponseEntity<TeamDeleteActiveParentTopicsDto> handle(ApplicationExceptions.TeamDeleteException e) {
        log.error("Exception: {}", e.getMessage(), value(EVENT, TEAM_DELETED_FAILURE));
        return new ResponseEntity<>(e.getTeamDeleteActiveParentTopicsDto() ,PRECONDITION_FAILED);
    }

    @ExceptionHandler(ApplicationExceptions.TopicCreationException.class)
    public ResponseEntity handle(ApplicationExceptions.TopicCreationException e) {
        log.error("TopicCreationException: {}", e.getMessage(),value(EVENT, BAD_REQUEST));
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationExceptions.TopicUpdateException.class)
    public ResponseEntity handle(ApplicationExceptions.TopicUpdateException e) {
        log.error("TopicCreationException: {}", e.getMessage(),value(EVENT, BAD_REQUEST));
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handle(Exception e) {
        log.error("Exception: {}", e.getMessage(), value(EVENT, UNCAUGHT_EXCEPTION));
        return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
    }

}
