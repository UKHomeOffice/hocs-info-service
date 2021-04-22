package uk.gov.digital.ho.hocs.info.client.notifyclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import uk.gov.digital.ho.hocs.info.client.notifyclient.dto.TeamRenameCommand;
import uk.gov.digital.ho.hocs.info.application.RequestData;

import java.util.Map;
import java.util.UUID;

import static net.logstash.logback.argument.StructuredArguments.value;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.EVENT;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.EXCEPTION;
import static uk.gov.digital.ho.hocs.info.client.notifyclient.dto.EventType.NOTIFY_EMAIL_FAILED;
import static uk.gov.digital.ho.hocs.info.client.notifyclient.dto.EventType.TEAM_RENAME_EMAIL_SENT;

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

    @Async
    public void sendTeamRenameEmail(UUID teamUUID, String oldDisplayName) {
        sendTeamRenameChangeEmailCommand(new TeamRenameCommand(teamUUID, oldDisplayName));
    }

    @Retryable(maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.delay}"))
    private void sendTeamRenameChangeEmailCommand(TeamRenameCommand command){
        try {
            Map<String, Object> queueHeaders = getQueueHeaders();
            producerTemplate.sendBodyAndHeaders(notifyQueue, objectMapper.writeValueAsString(command), queueHeaders);
            log.info("Sent team change email of type {}, correlationID: {}, UserID: {}", command.getCommand(), requestData.correlationId(), requestData.userId(), value(EVENT, TEAM_RENAME_EMAIL_SENT));
        } catch (Exception e) {
            logFailedToSendEmail(e);
        }
    }

    private void logFailedToSendEmail(Exception e){
        log.error("Failed to send email {}", value(EVENT, NOTIFY_EMAIL_FAILED), value(EXCEPTION, e));
    }

    private Map<String, Object> getQueueHeaders() {
        return Map.of(
        RequestData.CORRELATION_ID_HEADER, requestData.correlationId(),
        RequestData.USER_ID_HEADER, requestData.userId(),
        RequestData.USERNAME_HEADER, requestData.username(),
        RequestData.GROUP_HEADER, requestData.groups());
    }
}
