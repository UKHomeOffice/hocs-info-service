package uk.gov.digital.ho.hocs.info.client.notifyclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
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
    private final ProducerTemplate producerTemplate;
    private final ObjectMapper objectMapper;
    private final RequestData requestData;

    @Autowired
    public NotifyClient(ProducerTemplate producerTemplate,
                        @Value("${notify.queue}") String notifyQueue,
                        ObjectMapper objectMapper,
                        RequestData requestData) {
        this.producerTemplate = producerTemplate;
        this.notifyQueue = notifyQueue;
        this.objectMapper = objectMapper;
        this.requestData = requestData;
    }

    @Retryable(maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.delay}"))
    @Async
    public void sendTeamRenameEmail(UUID teamUUID, String oldDisplayName) {
        Assert.notNull(teamUUID, "teamUUID parameter must not be null");
        Assert.notNull(oldDisplayName, "oldDisplayName parameter must not be null");

        sendTeamCommand(new TeamRenameCommand(teamUUID, oldDisplayName));
    }

    @Retryable(maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.delay}"))
    @Async
    public void sendTeamActiveStatusEmail(UUID teamUUID, Boolean currentActiveStatus) {
        Assert.notNull(teamUUID, "teamUUID parameter must not be null");
        Assert.notNull(currentActiveStatus, "currentActiveStatus parameter must not be null");

        sendTeamCommand(new TeamActiveCommand(teamUUID, currentActiveStatus));
    }

    private <T extends TeamCommand> void sendTeamCommand(T command) {
        try {
            producerTemplate.sendBodyAndHeaders(notifyQueue, objectMapper.writeValueAsString(command), getQueueHeaders());
            log.info("Sent notify event: {}, correlationID: {}, UserID: {}",
                    command.getCommand(),
                    requestData.correlationId(),
                    requestData.userId(),
                    value(EVENT, command.getCommand()));
        } catch (CamelExecutionException | JsonProcessingException e) {
            log.error("Failed to send notify event:{}, correlationID:{}, UserID:{}",
                    command.getCommand(),
                    requestData.correlationId(),
                    requestData.userId(),
                    value(EVENT, command.getCommand()), value(EXCEPTION, e));
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> getQueueHeaders() {
        return Map.of(
                RequestData.CORRELATION_ID_HEADER, requestData.correlationId(),
                RequestData.USER_ID_HEADER, requestData.userId(),
                RequestData.USERNAME_HEADER, requestData.username(),
                RequestData.GROUP_HEADER, requestData.groups());
    }
}
