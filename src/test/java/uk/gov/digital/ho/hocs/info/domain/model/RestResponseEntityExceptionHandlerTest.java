package uk.gov.digital.ho.hocs.info.domain.model;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.exception.RestResponseEntityExceptionHandler;

import static junit.framework.TestCase.assertEquals;


public class RestResponseEntityExceptionHandlerTest {

    private RestResponseEntityExceptionHandler restResponseEntityExceptionHandler;

    @Before
    public void beforeTest(){
        restResponseEntityExceptionHandler = new RestResponseEntityExceptionHandler();
    }

    @Test
    public void handleEntityAlreadyExistsException(){
        String message = "Test Error Message";
        ApplicationExceptions.EntityAlreadyExistsException exception = new ApplicationExceptions.EntityAlreadyExistsException(message);

        ResponseEntity result = restResponseEntityExceptionHandler.handle(exception);

        assertEquals("Http code should be 409", 409, result.getStatusCode().value());
        assertEquals("Error message incorrect", message, result.getBody());

    }
}
