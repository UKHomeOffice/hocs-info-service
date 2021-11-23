package uk.gov.digital.ho.hocs.info.domain.exception;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import uk.gov.digital.ho.hocs.info.security.KeycloakException;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestResponseEntityExceptionHandlerTest {

    private RestResponseEntityExceptionHandler restResponseEntityExceptionHandler;

    @Before
    public void beforeTest(){
        restResponseEntityExceptionHandler = new RestResponseEntityExceptionHandler();
    }

    @Test
    public void handleEntityCreationException(){
        String msg = "Test Error msg";
        ApplicationExceptions.EntityCreationException exception = new ApplicationExceptions.EntityCreationException(msg);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 500", 500, result.getStatusCode().value());
        assertEquals("Error msg incorrect", msg, result.getBody());

    }

    @Test
    public void handleEntityAlreadyExistsException(){
        String msg = "Test Error msg";
        ApplicationExceptions.EntityAlreadyExistsException exception = new ApplicationExceptions.EntityAlreadyExistsException(msg);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 409", 409, result.getStatusCode().value());
        assertEquals("Error msg incorrect", msg, result.getBody());

    }

    @Test
    public void handleEntityNotFoundException(){
        String msg = "Test Error msg";
        ApplicationExceptions.EntityNotFoundException exception = new ApplicationExceptions.EntityNotFoundException(msg);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 404", 404, result.getStatusCode().value());
        assertEquals("Error msg incorrect", msg, result.getBody());

    }

    @Test
    public void handleStandardLineNotFoundException(){
        String msg = "Test Error msg";
        ApplicationExceptions.StandardLineNotFoundException exception = new ApplicationExceptions.StandardLineNotFoundException(msg);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 404", 404, result.getStatusCode().value());
        assertEquals("Error msg incorrect", msg, result.getBody());

    }

    @Test
    public void handleResourceServerException(){
        String msg = "Test Error msg";
        ApplicationExceptions.ResourceServerException exception = new ApplicationExceptions.ResourceServerException(msg, null);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 500", 500, result.getStatusCode().value());
        assertEquals("Error msg incorrect", msg, result.getBody());

    }

    @Test
    public void handleResourceNotFoundException(){
        String msg = "Test Error msg";
        ApplicationExceptions.ResourceNotFoundException exception = new ApplicationExceptions.ResourceNotFoundException(msg, null);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 404", 404, result.getStatusCode().value());
        assertEquals("Error msg incorrect", msg, result.getBody());

    }

    @Test
    public void handleMethodArgumentNotValidExceptionWithNoBindingErrors(){
        String msg = "Test Error msg";
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getMessage()).thenReturn(msg);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.hasErrors()).thenReturn(false);
        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 400", 400, result.getStatusCode().value());
        assertEquals("Error msg incorrect", msg, result.getBody());
    }

    @Test
    public void handleMethodArgumentNotValidExceptionWithBingingErrors(){
        String validationMessage = "Test Error msg";
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getMessage()).thenReturn("abc");
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.hasErrors()).thenReturn(true);
        List<ObjectError> errors = new ArrayList<>();
        errors.add(new ObjectError("objectName", validationMessage));
        when(bindingResult.getAllErrors()).thenReturn(errors);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 400", 400, result.getStatusCode().value());
        assertEquals("Error msg incorrect", validationMessage, result.getBody());
    }

    @Test
    public void handleHttpmsgConversionException(){
        String msg = "Test Error msg";
        HttpMessageConversionException exception = new HttpMessageConversionException(msg);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 400", 400, result.getStatusCode().value());
        assertEquals("Error msg incorrect", msg, result.getBody());

    }


    @Test
    public void handleHttpmsgNotReadableException(){
        String msg = "Test Error msg";
        HttpInputMessage httpInputMessage = null;

        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(msg, httpInputMessage);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 400", 400, result.getStatusCode().value());
        assertEquals("Error msg incorrect", msg, result.getBody());

    }


    @Test
    public void handleCorrespondentCreationException(){
        String msg = "Test Error msg";
        ApplicationExceptions.CorrespondentCreationException exception = new ApplicationExceptions.CorrespondentCreationException(msg);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 400", 400, result.getStatusCode().value());
        assertEquals("Error msg incorrect", msg, result.getBody());

    }

    @Test
    public void handleKeycloakExceptionWithoutHttpStatus(){
        String msg = "Test Error msg";
        KeycloakException exception = new KeycloakException(msg);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 500", 500, result.getStatusCode().value());
        assertEquals("Error msg incorrect", msg, result.getBody());
    }

    @Test
    public void handleKeycloakExceptionWithHttpStatus(){
        String msg = "Test Error msg";
        int httpStatusCode = 409;
        KeycloakException exception = new KeycloakException(msg, httpStatusCode);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be " + httpStatusCode, httpStatusCode, result.getStatusCode().value());
        assertEquals("Error msg incorrect", msg, result.getBody());

    }


    @Test
    public void handleTeamDeleteException(){
        String msg = "Test Error msg";

        ApplicationExceptions.TeamDeleteException exception = new ApplicationExceptions.TeamDeleteException(msg, null);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 412", 412, result.getStatusCode().value());

    }


    @Test
    public void handleTopicCreationException(){
        String msg = "Test Error msg";
        ApplicationExceptions.TopicCreationException exception = new ApplicationExceptions.TopicCreationException(msg);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 400", 400, result.getStatusCode().value());
        assertEquals("Error msg incorrect", msg, result.getBody());

    }


    @Test
    public void handleTopicUpdateException(){
        String msg = "Test Error msg";
        ApplicationExceptions.TopicUpdateException exception = new ApplicationExceptions.TopicUpdateException(msg);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 400", 400, result.getStatusCode().value());
        assertEquals("Error msg incorrect", msg, result.getBody());

    }

    @Test
    public void handleUserRemoveException(){
        String msg = "Test Error msg";
        ApplicationExceptions.UserRemoveException exception = new ApplicationExceptions.UserRemoveException(msg);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 409", 409, result.getStatusCode().value());
        assertEquals("Error msg incorrect", msg, result.getBody());

    }

    @Test
    public void handleException(){
        String msg = "Test Error msg";
        Exception exception = new Exception(msg);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 500", 500, result.getStatusCode().value());
        assertEquals("Error msg incorrect", msg, result.getBody());

    }
}
