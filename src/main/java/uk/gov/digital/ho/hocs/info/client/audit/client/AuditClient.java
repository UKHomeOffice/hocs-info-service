package uk.gov.digital.ho.hocs.info.client.audit.client;

import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.digital.ho.hocs.info.api.dto.PermissionDto;
import uk.gov.digital.ho.hocs.info.application.RequestData;
import uk.gov.digital.ho.hocs.info.application.aws.util.SnsStringMessageAttributeValue;
import uk.gov.digital.ho.hocs.info.client.audit.client.dto.CreateAuditRequest;
import uk.gov.digital.ho.hocs.info.client.audit.client.dto.EventType;
import uk.gov.digital.ho.hocs.info.domain.model.*;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static net.logstash.logback.argument.StructuredArguments.value;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.AUDIT_EVENT_CREATED;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.AUDIT_FAILED;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.EVENT;

@Slf4j
@Component
public class AuditClient {

    private final String auditQueue;
    private final String raisingService;
    private final String namespace;

    private final AmazonSNSAsync auditSearchSnsClient;
    private final ObjectMapper objectMapper;
    private final RequestData requestData;
    private final JsonArrayBuilder permissionArray;
    private static final String EVENT_TYPE_HEADER = "event_type";
    private static final String TOPIC = "topicUUID";
    private static final String ACTIVE = "active";
    private static final String STAGE_TYPE = "stageType";
    private static final String TEAM_UUID = "teamUUID";
    private static final String CASE_TYPE = "caseType";
    private static final String DISPLAY_NAME = "displayName";


    @Autowired
    public AuditClient(AmazonSNSAsync auditSearchSnsClient,
                       @Value("${audit.queue}") String auditQueue,
                       @Value("${auditing.deployment.name}") String raisingService,
                       @Value("${auditing.deployment.namespace}") String namespace,
                       ObjectMapper objectMapper,
                       RequestData requestData) {
        this.auditSearchSnsClient = auditSearchSnsClient;
        this.auditQueue = auditQueue;
        this.raisingService = raisingService;
        this.namespace = namespace;
        this.objectMapper = objectMapper;
        this.requestData = requestData;
        permissionArray = Json.createArrayBuilder();
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

    public void addUsersToTeamAudit(String userUUIDs, Team team) {
        String auditPayload = Json.createObjectBuilder().add(TEAM_UUID, team.getUuid().toString()).add("userUUIDs", userUUIDs).build().toString();
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


    public void setTeamActivationFlag(Team team) {
        String auditPayload = Json.createObjectBuilder()
                .add(ACTIVE, team.isActive())
                .add(TEAM_UUID, team.getUuid().toString()
                ).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.SET_TEAM_ACTIVATION_FLAG.toString());
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

        try {
            var publishRequest = new PublishRequest(auditQueue, objectMapper.writeValueAsString(request))
                    .withMessageAttributes(getQueueHeaders(request.getType()));

            auditSearchSnsClient.publish(publishRequest);
            log.info("Create audit for event {}, correlationID: {}, UserID: {}", request.getType(), requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create audit event {} for reason {}", request.getType(), e, value(EVENT, AUDIT_FAILED));
        }
    }

    private CreateAuditRequest generateAuditRequest(String auditPayload, String eventType) {
        return new CreateAuditRequest(
                requestData.correlationId(),
                raisingService,
                auditPayload,
                namespace,
                LocalDateTime.now(),
                eventType,
                requestData.userId());
    }

    private Map<String, MessageAttributeValue> getQueueHeaders(String eventType) {
        return Map.of(
                EVENT_TYPE_HEADER, new SnsStringMessageAttributeValue(eventType),
                RequestData.CORRELATION_ID_HEADER, new SnsStringMessageAttributeValue(requestData.correlationId()),
                RequestData.USER_ID_HEADER, new SnsStringMessageAttributeValue(requestData.userId()),
                RequestData.USERNAME_HEADER, new SnsStringMessageAttributeValue(requestData.username()),
                RequestData.GROUP_HEADER, new SnsStringMessageAttributeValue(requestData.groups()));
    }
}
