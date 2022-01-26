package uk.gov.digital.ho.hocs.info.client.notifyclient;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.digital.ho.hocs.info.application.aws.util.SqsStringMessageAttributeValue;
import uk.gov.digital.ho.hocs.info.client.notifyclient.dto.TeamActiveCommand;
import uk.gov.digital.ho.hocs.info.client.notifyclient.dto.TeamCommand;
import uk.gov.digital.ho.hocs.info.client.notifyclient.dto.TeamRenameCommand;
import uk.gov.digital.ho.hocs.info.application.RequestData;

import java.util.Map;
import java.util.UUID;

import static net.logstash.logback.argument.StructuredArguments.value;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.EVENT;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.EXCEPTION;

@Slf4j
@Component
public class NotifyClient {

    private final String notifyQueue;
    private final AmazonSQSAsync notifyAsyncClient;
    private final ObjectMapper objectMapper;
    private final RequestData requestData;

    @Autowired
    public NotifyClient(AmazonSQSAsync notifyAsyncClient,
                        @Value("${notify.queue}") String notifyQueue,
                        ObjectMapper objectMapper,
                        RequestData requestData) {
        this.notifyAsyncClient = notifyAsyncClient;
        this.notifyQueue = notifyQueue;
        this.objectMapper = objectMapper;
        this.requestData = requestData;
    }

    public void sendTeamRenameEmail(UUID teamUUID, String oldDisplayName) {
        Assert.notNull(teamUUID, "teamUUID parameter must not be null");
        Assert.notNull(oldDisplayName, "oldDisplayName parameter must not be null");

        sendTeamCommand(new TeamRenameCommand(teamUUID, oldDisplayName));
    }

    public void sendTeamActiveStatusEmail(UUID teamUUID, Boolean currentActiveStatus) {
        Assert.notNull(teamUUID, "teamUUID parameter must not be null");
        Assert.notNull(currentActiveStatus, "currentActiveStatus parameter must not be null");

        sendTeamCommand(new TeamActiveCommand(teamUUID, currentActiveStatus));
    }

    private <T extends TeamCommand> void sendTeamCommand(T command) {
        try {
            var messageRequest =
                    new SendMessageRequest(notifyQueue, objectMapper.writeValueAsString(command))
                            .withMessageAttributes(getQueueHeaders());

            notifyAsyncClient.sendMessage(messageRequest);
            log.info("Sent notify event: {}, correlationID: {}, UserID: {}",
                    command.getCommand(),
                    requestData.correlationId(),
                    requestData.userId(),
                    value(EVENT, command.getCommand()));
        } catch (JsonProcessingException e) {
            log.error("Failed to send notify event:{}, correlationID:{}, UserID:{}",
                    command.getCommand(),
                    requestData.correlationId(),
                    requestData.userId(),
                    value(EVENT, command.getCommand()), value(EXCEPTION, e));
            throw new RuntimeException(e);
        }
    }

    private Map<String, MessageAttributeValue> getQueueHeaders() {
        return Map.of(
                RequestData.CORRELATION_ID_HEADER, new SqsStringMessageAttributeValue(requestData.correlationId()),
                RequestData.USER_ID_HEADER, new SqsStringMessageAttributeValue(requestData.userId()),
                RequestData.USERNAME_HEADER, new SqsStringMessageAttributeValue(requestData.username()),
                RequestData.GROUP_HEADER, new SqsStringMessageAttributeValue(requestData.groups()));
    }
}
