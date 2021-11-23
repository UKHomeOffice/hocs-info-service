package uk.gov.digital.ho.hocs.info.client.notifiyclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.ProducerTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.application.RequestData;
import uk.gov.digital.ho.hocs.info.application.SpringConfiguration;
import uk.gov.digital.ho.hocs.info.client.notifyclient.NotifyClient;
import uk.gov.digital.ho.hocs.info.client.notifyclient.dto.TeamActiveCommand;
import uk.gov.digital.ho.hocs.info.client.notifyclient.dto.TeamRenameCommand;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NotifyClientTest {

    @Mock
    private RequestData requestData;

    @Mock
    ProducerTemplate producerTemplate;

    private ObjectMapper mapper;

    private final SpringConfiguration configuration = new SpringConfiguration();
    private final String notifyQueue ="notify-queue";

    @Captor
    ArgumentCaptor jsonCaptor;

    @Captor
    ArgumentCaptor<HashMap<String,Object>> headerCaptor;

    private NotifyClient notifyClient;

    @Before
    public void setup() {
        when(requestData.correlationId()).thenReturn(randomUUID().toString());
        when(requestData.userId()).thenReturn("__userID__");
        when(requestData.groups()).thenReturn("__group__");
        when(requestData.username()).thenReturn("__anonymous__");

        mapper = configuration.initialiseObjectMapper();
        notifyClient = new NotifyClient(producerTemplate, notifyQueue, mapper, requestData);
    }

    @Test
    public void shouldSetRenameTeamDataFields() throws IOException {
        UUID teamUUID = UUID.randomUUID();
        String oldDisplayName = "TEST";
        String command = "team_rename";

        notifyClient.sendTeamRenameEmail(teamUUID, oldDisplayName);

        verify(producerTemplate, times(1)).sendBodyAndHeaders(eq(notifyQueue), jsonCaptor.capture(), any());
        TeamRenameCommand request = mapper.readValue((String)jsonCaptor.getValue(), TeamRenameCommand.class);

        assertThat(request.getCommand()).isEqualTo(command);
        assertThat(request.getOldDisplayName()).isEqualTo(oldDisplayName);
        assertThat(request.getTeamUUID()).isEqualTo(teamUUID);
    }

    @Test
    public void shouldSendTeamActiveEmail() throws IOException {
        UUID teamUUID = UUID.randomUUID();
        notifyClient.sendTeamActiveStatusEmail(teamUUID, Boolean.FALSE);

        verify(producerTemplate, times(1)).sendBodyAndHeaders(eq(notifyQueue), jsonCaptor.capture(), any());
        TeamActiveCommand request = mapper.readValue((String)jsonCaptor.getValue(), TeamActiveCommand.class);

        assertThat(request.getCommand()).isEqualTo("team_active");
        assertThat(request.getCurrentActiveStatus()).isEqualTo(Boolean.FALSE);
        assertThat(request.getTeamUUID()).isEqualTo(teamUUID);
    }

    @Test
    public void shouldSetHeaders()  {
        Map<String, Object> expectedHeaders = Map.of(
                RequestData.CORRELATION_ID_HEADER, requestData.correlationId(),
                RequestData.USER_ID_HEADER, requestData.userId(),
                RequestData.USERNAME_HEADER, requestData.username(),
                RequestData.GROUP_HEADER, requestData.groups());

        UUID currentUser = UUID.randomUUID();
        UUID newUser = UUID.randomUUID();

        notifyClient.sendTeamRenameEmail(UUID.randomUUID(), "");
        verify(producerTemplate, times(1))
                .sendBodyAndHeaders(eq(notifyQueue), any(), headerCaptor.capture());
        Map headers = headerCaptor.getValue();

        assertThat(headers).containsAllEntriesOf(expectedHeaders);
    }

}