package uk.gov.digital.ho.hocs.info.client.notifiyclient;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.application.RequestData;
import uk.gov.digital.ho.hocs.info.application.aws.util.SqsStringMessageAttributeValue;
import uk.gov.digital.ho.hocs.info.client.notifyclient.NotifyClient;
import uk.gov.digital.ho.hocs.info.client.notifyclient.dto.TeamActiveCommand;
import uk.gov.digital.ho.hocs.info.client.notifyclient.dto.TeamRenameCommand;
import uk.gov.digital.ho.hocs.info.utils.BaseAwsTest;

import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({ "local", "integration" })
public class NotifyClientTest extends BaseAwsTest {

    @Captor
    ArgumentCaptor<SendMessageRequest> messageCaptor;

    @SpyBean
    private AmazonSQSAsync notifySqsClient;

    @MockBean(name = "requestData")
    private RequestData requestData;

    private ResultCaptor<SendMessageResult> sqsMessageResult;

    @Autowired
    private NotifyClient notifyClient;

    @Before
    public void setup() {
        when(requestData.correlationId()).thenReturn(UUID.randomUUID().toString());
        when(requestData.userId()).thenReturn("some user id");
        when(requestData.groups()).thenReturn("some groups");
        when(requestData.username()).thenReturn("some username");

        sqsMessageResult = new ResultCaptor<>();
        doAnswer(sqsMessageResult).when(notifySqsClient).sendMessage(any());
    }

    @Test
    public void shouldSetRenameTeamDataFields() {
        UUID teamUUID = UUID.randomUUID();
        String oldDisplayName = "TEST";

        var teamRenameCommand = new TeamRenameCommand(teamUUID, oldDisplayName);
        notifyClient.sendTeamRenameEmail(teamUUID, oldDisplayName);

        assertSqsValue(teamRenameCommand);
    }

    @Test
    public void shouldSendTeamActiveEmail() {
        UUID teamUUID = UUID.randomUUID();

        var teamActiveCommand = new TeamActiveCommand(teamUUID, Boolean.FALSE);
        notifyClient.sendTeamActiveStatusEmail(teamUUID, Boolean.FALSE);

        assertSqsValue(teamActiveCommand);
    }

    @Test
    public void shouldSetHeaders() {
        Map<String, MessageAttributeValue> expectedHeaders = Map.of(RequestData.CORRELATION_ID_HEADER,
            new SqsStringMessageAttributeValue(requestData.correlationId()), RequestData.USER_ID_HEADER,
            new SqsStringMessageAttributeValue(requestData.userId()), RequestData.USERNAME_HEADER,
            new SqsStringMessageAttributeValue(requestData.username()), RequestData.GROUP_HEADER,
            new SqsStringMessageAttributeValue(requestData.groups()));

        UUID teamUUID = UUID.randomUUID();
        String oldDisplayName = "TEST";

        notifyClient.sendTeamRenameEmail(teamUUID, oldDisplayName);

        verify(notifySqsClient).sendMessage(messageCaptor.capture());
        Assertions.assertEquals(messageCaptor.getValue().getMessageAttributes(), expectedHeaders);
    }

    private void assertSqsValue(Object command) {
        Assertions.assertNotNull(sqsMessageResult);

        // getMessageMd5 - toString strips leading zeros, 31/32 matched is close enough in this instance
        Assertions.assertTrue(sqsMessageResult.getResult().getMD5OfMessageBody().contains(getMessageMd5(command)));
    }

}
