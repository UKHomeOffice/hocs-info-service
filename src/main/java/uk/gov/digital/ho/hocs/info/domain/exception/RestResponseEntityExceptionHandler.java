package uk.gov.digital.ho.hocs.info.domain.exception;

import static net.logstash.logback.argument.StructuredArguments.value;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.EVENT;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.EXCEPTION;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.KEYCLOAK_FAILURE;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.TEAM_DELETED_FAILURE;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.UNCAUGHT_EXCEPTION;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uk.gov.digital.ho.hocs.info.api.dto.TeamDeleteActiveParentTopicsDto;
import uk.gov.digital.ho.hocs.info.security.KeycloakException;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationExceptions.EntityCreationException.class)
    public ResponseEntity handle(ApplicationExceptions.EntityCreationException e) {
        log.error("ApplicationExceptions.EntityCreationException: {}, Event: {}", e.getMessage(),
            value(EVENT, e.getEvent()));
        return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApplicationExceptions.EntityAlreadyExistsException.class)
    public ResponseEntity handle(ApplicationExceptions.EntityAlreadyExistsException e) {
        log.error("ApplicationExceptions.EntityAlreadyExistsException: {}, Event: {}", e.getMessage(),
            value(EVENT, e.getEvent()));
        return new ResponseEntity<>(e.getMessage(), CONFLICT);
    }

    @ExceptionHandler(ApplicationExceptions.UserAlreadyExistsException.class)
    public ResponseEntity handle(ApplicationExceptions.UserAlreadyExistsException e) {
        log.info("ApplicationExceptions.UserAlreadyExistsException: {}", e.getMessage(), value(EVENT, e.getEvent()));
        return new ResponseEntity<>(e.getMessage(), CONFLICT);
    }

    @ExceptionHandler(ApplicationExceptions.EntityNotFoundException.class)
    public ResponseEntity handle(ApplicationExceptions.EntityNotFoundException e) {
        log.error("ApplicationExceptions.EntityNotFoundException: {}, Event: {}", e.getMessage(),
            value(EVENT, e.getEvent()));
        return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(ApplicationExceptions.ResourceServerException.class)
    public ResponseEntity handle(ApplicationExceptions.ResourceServerException e) {
        log.error("ApplicationExceptions.ResourceServerException: {}, Event: {}", e.getMessage(),
            value(EVENT, e.getEvent()));
        return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApplicationExceptions.ResourceNotFoundException.class)
    public ResponseEntity handle(ApplicationExceptions.ResourceNotFoundException e) {
        log.error("ApplicationExceptions.ResourceNotFoundException: {}, Event: {}", e.getMessage(),
            value(EVENT, e.getEvent()));
        return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(ApplicationExceptions.EntityPermissionException.class)
    public ResponseEntity handle(ApplicationExceptions.EntityPermissionException e) {
        log.error("ApplicationExceptions.EntityPermissionException: {}, Event: {}", e.getMessage(),
            value(EVENT, e.getEvent()));
        return new ResponseEntity<>(e.getMessage(), FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handle(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: {}, Event: {}", e.getMessage(), value(EVENT, BAD_REQUEST));
        String errorMessage = e.getMessage();
        if (e.getBindingResult().hasErrors()) {
            List<String> validationErrors = e.getBindingResult().getAllErrors().stream().map(
                DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
            errorMessage = String.join(", ", validationErrors);
        }
        return new ResponseEntity<>(errorMessage, BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity handle(HttpMessageConversionException e) {
        log.error("HttpMessageConversionException: {}, Event: {}, Exception: {}", e.getMessage(),
            value(EVENT, BAD_REQUEST), value(EXCEPTION, e));
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handle(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException: {}, Event: {}, Exception: {}", e.getMessage(),
            value(EVENT, BAD_REQUEST), value(EXCEPTION, e));
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationExceptions.CorrespondentCreationException.class)
    public ResponseEntity handle(ApplicationExceptions.CorrespondentCreationException e) {
        log.error("DataIntegrityViolationException: {}, Event: {}, Exception: {}", e.getMessage(),
            value(EVENT, BAD_REQUEST), value(EXCEPTION, e));
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(KeycloakException.class)
    public ResponseEntity handle(KeycloakException e) {
        log.error("Keycloak exception: {}, Event: {}", e.getMessage(), value(EVENT, KEYCLOAK_FAILURE));
        HttpStatus httpStatus = e.getHttpStatus() != null
            ? HttpStatus.valueOf(e.getHttpStatus())
            : INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(e.getMessage(), httpStatus);
    }

    @ExceptionHandler(ApplicationExceptions.TeamDeleteException.class)
    public ResponseEntity<TeamDeleteActiveParentTopicsDto> handle(ApplicationExceptions.TeamDeleteException e) {
        log.error("Exception: {}, Event: {}", e.getMessage(), value(EVENT, TEAM_DELETED_FAILURE));
        return new ResponseEntity<>(e.getTeamDeleteActiveParentTopicsDto(), PRECONDITION_FAILED);
    }

    @ExceptionHandler(ApplicationExceptions.TopicCreationException.class)
    public ResponseEntity handle(ApplicationExceptions.TopicCreationException e) {
        log.error("TopicCreationException: {}, Event: {}", e.getMessage(), value(EVENT, BAD_REQUEST));
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationExceptions.TopicUpdateException.class)
    public ResponseEntity handle(ApplicationExceptions.TopicUpdateException e) {
        log.error("TopicCreationException: {}, Event: {}", e.getMessage(), value(EVENT, BAD_REQUEST));
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationExceptions.UserRemoveException.class)
    public ResponseEntity handle(ApplicationExceptions.UserRemoveException e) {
        log.error("UserRemoveException: {}, Event: {}", e.getMessage(), value(EVENT, CONFLICT));
        return new ResponseEntity<>(e.getMessage(), CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handle(Exception e) {
        Writer stackTraceWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stackTraceWriter);
        e.printStackTrace(printWriter);
        log.error("Exception: {}, Event: {}, Stack: {}", e.getMessage(), value(EVENT, UNCAUGHT_EXCEPTION),
            stackTraceWriter.toString());
        return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
    }

}
