package uk.gov.digital.ho.hocs.info;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.application.RequestData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RequestDataTest {

    @Mock
    private HttpServletRequest mockHttpServletRequest;

    @Mock
    private HttpServletResponse mockHttpServletResponse;

    @Mock
    private Object mockHandler;

    private RequestData requestData;

    @Before
    public void setup() {
        requestData = new RequestData();
    }

    @Test
    public void shouldDefaultRequestData() {
        requestData.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);

        assertThat(requestData.correlationId()).isNotNull();
        assertThat(requestData.userId()).isEqualTo("anonymous");
        assertThat(requestData.username()).isEqualTo("anonymous");
    }

    @Test
    public void shouldUseCorrelationIdFromRequest() {
        when(mockHttpServletRequest.getHeader("X-Correlation-Id")).thenReturn("some correlation id");

        requestData.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);

        assertThat(requestData.correlationId()).isEqualTo("some correlation id");
    }

    @Test
    public void shouldUseUserIdFromRequest() {
        when(mockHttpServletRequest.getHeader("X-Auth-UserId")).thenReturn("some user id");

        requestData.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);

        assertThat(requestData.userId()).isEqualTo("some user id");
    }

    @Test
    public void shouldUseUsernameFromRequest() {
        when(mockHttpServletRequest.getHeader("X-Auth-Username")).thenReturn("some username");

        requestData.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);

        assertThat(requestData.username()).isEqualTo("some username");
    }

    @Test
    public void shouldWriteUsernameToResponseHeaders() {
        when(mockHttpServletRequest.getHeader("X-Correlation-Id")).thenReturn("some correlation id");
        when(mockHttpServletRequest.getHeader("X-Auth-UserId")).thenReturn("some user id");
        when(mockHttpServletRequest.getHeader("X-Auth-Username")).thenReturn("some username");

        requestData.preHandle(mockHttpServletRequest, mockHttpServletResponse, mockHandler);
        requestData.afterCompletion(mockHttpServletRequest, mockHttpServletResponse, mockHandler, null);

        verify(mockHttpServletResponse).setHeader(RequestData.USER_ID_HEADER, "some user id");
        verify(mockHttpServletResponse).setHeader(RequestData.USERNAME_HEADER, "some username");
        verify(mockHttpServletResponse).setHeader(RequestData.CORRELATION_ID_HEADER, "some correlation id");
    }

}
