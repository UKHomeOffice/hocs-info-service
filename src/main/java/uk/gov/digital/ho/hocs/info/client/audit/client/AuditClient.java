package uk.gov.digital.ho.hocs.info.client.audit.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.digital.ho.hocs.info.api.dto.PermissionDto;
import uk.gov.digital.ho.hocs.info.application.RequestData;
import uk.gov.digital.ho.hocs.info.client.audit.client.dto.CreateAuditRequest;
import uk.gov.digital.ho.hocs.info.client.audit.client.dto.EventType;
import uk.gov.digital.ho.hocs.info.domain.model.*;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import java.time.LocalDateTime;
import java.util.*;

import static net.logstash.logback.argument.StructuredArguments.value;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.*;

@Component
public class AuditClient {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AuditClient.class);
    private final String auditQueue;
    private final String raisingService;
    private final String namespace;

    private final ProducerTemplate producerTemplate;
    private final ObjectMapper objectMapper;
    private final RequestData requestData;
    private final JsonArrayBuilder permissionArray;
    private static final String EVENT_TYPE_HEADER = "event_type";
    private static final String TOPIC = "topicUUID";
    private static final String STAGE_TYPE = "stageType";
    private static final String TEAM_UUID = "teamUUID";
    private static final String CASE_TYPE = "caseType";
    private static final String DISPLAY_NAME = "displayName";

    @Autowired
    public AuditClient(ProducerTemplate producerTemplate,
                       @Value("${audit.queue}") String auditQueue,
                       @Value("${auditing.deployment.name}") String raisingService,
                       @Value("${auditing.deployment.namespace}") String namespace,
                       ObjectMapper objectMapper,
                       RequestData requestData) {
        this.producerTemplate = producerTemplate;
        this.auditQueue = auditQueue;
        this.raisingService = raisingService;
        this.namespace = namespace;
        this.objectMapper = objectMapper;
        this.requestData = requestData;
        this.permissionArray = Json.createArrayBuilder();

        log.info("Audit client initialisation, auditQueue: {}, raisingService: {}, namespace: {}", auditQueue, raisingService, namespace);
    }

    public void createTeamAudit(Team team) {
        String auditPayload = Json.createObjectBuilder().add(TEAM_UUID, team.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.CREATE_TEAM.toString());
        sendAuditMessage(request);
    }

    public void renameTeamAudit(Team team) {
        String auditPayload = Json.createObjectBuilder().add(TEAM_UUID, team.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.RENAME_TEAM.toString());
        sendAuditMessage(request);
    }

    public void deleteTeamAudit(Team team) {
        String auditPayload = Json.createObjectBuilder().add(TEAM_UUID, team.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.DELETE_TEAM.toString());
        sendAuditMessage(request);
    }

    public void addUserToTeamAudit(UUID userUUID, Team team) {
        String auditPayload = Json.createObjectBuilder().add(TEAM_UUID, team.getUuid().toString()).add("userUUID", userUUID.toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.ADD_USER_TO_TEAM.toString());
        sendAuditMessage(request);
    }


    public void moveToNewUnitAudit(String teamUUID, String oldUnitUUID, String newUnitUUID) {
        String auditPayload = Json.createObjectBuilder().add(TEAM_UUID, teamUUID).add("oldUnit", oldUnitUUID).add("newUnit", newUnitUUID).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.MOVE_TEAM_TO_UNIT.toString());
        sendAuditMessage(request);
    }

    public void updateTeamPermissionsAudit(Set<PermissionDto> permissions) {
        for (PermissionDto permission : permissions) {
            permissionArray.add(Json.createObjectBuilder().add(CASE_TYPE, permission.getCaseTypeCode()).add("accessLevel", permission.getAccessLevel().toString()).build());
        }
        String auditPayload = Json.createObjectBuilder().add("permissions", permissionArray).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.UPDATE_TEAM_PERMISSIONS.toString());
        sendAuditMessage(request);
    }

    public void deleteTeamPermissionsAudit(Set<PermissionDto> permissions) {
        for (PermissionDto permission : permissions) {
            permissionArray.add(Json.createObjectBuilder().add(CASE_TYPE, permission.getCaseTypeCode()).add("accessLevel", permission.getAccessLevel().toString()).build());
        }
        String auditPayload = Json.createObjectBuilder().add("permissions", permissionArray).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REMOVE_PERMISSIONS_FROM_TEAM.toString());
        sendAuditMessage(request);
    }

    public void removeUserFromTeamAudit(UUID userUUID, UUID teamUUID) {
        String auditPayload = Json.createObjectBuilder().add(TEAM_UUID, teamUUID.toString()).add("userUUID", userUUID.toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REMOVE_USER_FROM_TEAM.toString());
        sendAuditMessage(request);
    }

    public void createTopicAudit(Topic topic) {
        String auditPayload = Json.createObjectBuilder()
                .add(TOPIC, topic.getUuid().toString())
                .add(DISPLAY_NAME, topic.getDisplayName())
                .add("parentTopicUUID", topic.getParentTopic().toString())
                .build()
                .toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.CREATE_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void createParentTopicAudit(ParentTopic parentTopic) {
        String auditPayload = Json.createObjectBuilder()
                .add(TOPIC, parentTopic.getUuid().toString())
                .add(DISPLAY_NAME, parentTopic.getDisplayName())
                .build()
                .toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.CREATE_PARENT_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void deleteTopicAudit(Topic topic) {
        String auditPayload = Json.createObjectBuilder()
                .add(TOPIC, topic.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REMOVE_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void deleteParentTopicAudit(ParentTopic parentTopic) {
        String auditPayload = Json.createObjectBuilder()
                .add(TOPIC, parentTopic.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REMOVE_PARENT_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void reactivateTopicAudit(Topic topic) {
        String auditPayload = Json.createObjectBuilder()
                .add(TOPIC, topic.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REACTIVATE_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void reactivateParentTopicAudit(ParentTopic parentTopic) {
        String auditPayload = Json.createObjectBuilder()
                .add(TOPIC, parentTopic.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REACTIVATE_PARENT_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void updateTopicParentAudit(Topic topic) {
        String auditPayload = Json.createObjectBuilder()
                .add(TOPIC, topic.getUuid().toString())
                .add("newParentTopicUUID", topic.getParentTopic().toString())
                .build()
                .toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.UPDATE_TOPIC_PARENT.toString());
        sendAuditMessage(request);
    }

    public void addTeamToTopicAudit(TeamLink teamLink) {
        String auditPayload = Json.createObjectBuilder()
                .add(TOPIC, teamLink.getLinkValue())
                .add(TEAM_UUID, teamLink.getResponsibleTeamUUID().toString())
                .add(CASE_TYPE, teamLink.getCaseType())
                .add(STAGE_TYPE, teamLink.getStageType())
                .build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.ADD_TEAM_TO_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void updateTeamForTopicAudit(TeamLink teamLink, UUID oldTeamUUID) {
        String auditPayload = Json.createObjectBuilder()
                .add(TOPIC, teamLink.getLinkValue())
                .add(TEAM_UUID, teamLink.getResponsibleTeamUUID().toString())
                .add(CASE_TYPE, teamLink.getCaseType())
                .add(STAGE_TYPE, teamLink.getStageType())
                .add("oldTeamUUID", oldTeamUUID.toString())
                .build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.UPDATE_TEAM_FOR_TOPIC.toString());
        sendAuditMessage(request);
    }


    public void createCorrespondentType(CorrespondentType correspondentType) {
        String auditPayload = Json.createObjectBuilder()
                .add("correspondentTypeUUID", correspondentType.getUuid().toString())
                .add(DISPLAY_NAME, correspondentType.getDisplayName())
                .add("type", correspondentType.getType())
                .build()
                .toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.CREATE_CORRESPONDENT_TYPE.toString());
        sendAuditMessage(request);
    }

    private void sendAuditMessage(CreateAuditRequest request) {
        Map<String, Object> queueHeaders = getQueueHeaders(request.getType());

        try {
            producerTemplate.sendBodyAndHeaders(auditQueue, objectMapper.writeValueAsString(request), queueHeaders);
            log.info("Create audit for event, type: {}, correlationID: {}, UserID: {}, event: {}", request.getType(), requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create audit event, type: {}, for reason {}, event: {}", request.getType(), e, value(EVENT, AUDIT_FAILED));
            log.error("Failed to create audit event, Exception stackTrace: {}", Arrays.toString(e.getStackTrace()));
            if (e instanceof CamelExecutionException) {
                log.error("Failed to create audit event, CamelExecutionException stackTrace: {}", Arrays.toString(((CamelExecutionException) e).getExchange().getException().getStackTrace()));
            }
        }
    }

    private CreateAuditRequest generateAuditRequest(String auditPayload, String eventType) {
        log.info("Generate audit request with payload: {}, eventType: {}", auditPayload, eventType);

        CreateAuditRequest request = new CreateAuditRequest(
                requestData.correlationId(),
                raisingService,
                auditPayload,
                namespace,
                LocalDateTime.now(),
                eventType,
                requestData.userId());

        log.info("Generate audit request result: {}", request);

        return request;
    }

    private Map<String, Object> getQueueHeaders(String eventType) {
        Map<String, Object> headers = new HashMap<>();

        log.info("Get queue headers with eventType: {}", eventType);

        headers.put(EVENT_TYPE_HEADER, eventType);
        headers.put(RequestData.CORRELATION_ID_HEADER, requestData.correlationId());
        headers.put(RequestData.USER_ID_HEADER, requestData.userId());
        headers.put(RequestData.USERNAME_HEADER, requestData.username());
        headers.put(RequestData.GROUP_HEADER, requestData.groups());

        log.info("Get queue headers with headers: correlationId: {}, userId: {}, username: {}, groups: {}",
                requestData.correlationId(), requestData.userId(), requestData.username(), requestData.groups());

        return headers;
    }
}