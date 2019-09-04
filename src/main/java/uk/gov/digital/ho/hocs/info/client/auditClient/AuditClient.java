package uk.gov.digital.ho.hocs.info.client.auditClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.digital.ho.hocs.info.api.dto.PermissionDto;
import uk.gov.digital.ho.hocs.info.application.RequestData;
import uk.gov.digital.ho.hocs.info.client.auditClient.dto.CreateAuditRequest;
import uk.gov.digital.ho.hocs.info.client.auditClient.dto.EventType;
import uk.gov.digital.ho.hocs.info.domain.model.*;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import java.time.LocalDateTime;
import java.util.HashMap;
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

    private final ProducerTemplate producerTemplate;
    private final ObjectMapper objectMapper;
    private final RequestData requestData;
    private final JsonArrayBuilder permissionArray;
    private final String EVENT_TYPE_HEADER ="event_type";


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
        permissionArray = Json.createArrayBuilder();
    }

    public void createConstituencyAudit(Constituency constituency) {
        String auditPayload = Json.createObjectBuilder()
                .add("constituencyUUID", constituency.getUuid().toString())
                .add("constituencyName", constituency.getConstituencyName())
                .build()
                .toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.CREATE_CONSTITUENCY.toString());
        sendAuditMessage(request);
    }

    public void deleteConstituencyAudit(Constituency constituency) {
        String auditPayload = Json.createObjectBuilder()
                .add("constituencyUUID", constituency.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REMOVE_CONSTITUENCY.toString());
        sendAuditMessage(request);
    }

    public void reactivateConstituencyAudit(Constituency constituency) {
        String auditPayload = Json.createObjectBuilder()
                .add("constituencyUUID", constituency.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REACTIVATE_CONSTITUENCY.toString());
        sendAuditMessage(request);
    }

    public void createTeamAudit(Team team) {
        String auditPayload = Json.createObjectBuilder().add("teamUUID", team.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.CREATE_TEAM.toString());
        sendAuditMessage(request);
    }

    public void renameTeamAudit(Team team) {
        String auditPayload = Json.createObjectBuilder().add("teamUUID", team.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.RENAME_TEAM.toString());
        sendAuditMessage(request);
    }

    public void deleteTeamAudit(Team team) {
        String auditPayload = Json.createObjectBuilder().add("teamUUID", team.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.DELETE_TEAM.toString());
        sendAuditMessage(request);
    }

    public void addUserToTeamAudit(UUID userUUID, Team team) {
        String auditPayload = Json.createObjectBuilder().add("teamUUID", team.getUuid().toString()).add("userUUID", userUUID.toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.ADD_USER_TO_TEAM.toString());
        sendAuditMessage(request);
    }


    public void moveToNewUnitAudit(String teamUUID, String oldUnitUUID, String newUnitUUID) {
        String auditPayload = Json.createObjectBuilder().add("teamUUID", teamUUID).add("oldUnit", oldUnitUUID).add("newUnit", newUnitUUID).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.MOVE_TEAM_TO_UNIT.toString());
        sendAuditMessage(request);
    }

    public void updateTeamPermissionsAudit(UUID teamUUID, Set<PermissionDto> permissions) {
        for (PermissionDto permission : permissions) {
            permissionArray.add(Json.createObjectBuilder().add("caseType", permission.getCaseTypeCode()).add("accessLevel", permission.getAccessLevel().toString()).build());
        }
        String auditPayload = Json.createObjectBuilder().add("permissions", permissionArray).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.UPDATE_TEAM_PERMISSIONS.toString());
        sendAuditMessage(request);
    }

    public void deleteTeamPermissionsAudit(UUID teamUUID, Set<PermissionDto> permissions) {
        for (PermissionDto permission : permissions) {
            permissionArray.add(Json.createObjectBuilder().add("caseType", permission.getCaseTypeCode()).add("accessLevel", permission.getAccessLevel().toString()).build());
        }
        String auditPayload = Json.createObjectBuilder().add("permissions", permissionArray).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REMOVE_PERMISSIONS_FROM_TEAM.toString());
        sendAuditMessage(request);
    }

    public void removeUserFromTeamAudit(UUID userUUID, UUID teamUUID) {
        String auditPayload = Json.createObjectBuilder().add("teamUUID", teamUUID.toString()).add("userUUID", userUUID.toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REMOVE_USER_FROM_TEAM.toString());
        sendAuditMessage(request);
    }

    public void createTopicAudit(Topic topic) {
        String auditPayload = Json.createObjectBuilder()
                .add("topicUUID", topic.getUuid().toString())
                .add("displayName", topic.getDisplayName())
                .add("parentTopicUUID", topic.getParentTopic().toString())
                .build()
                .toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.CREATE_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void createParentTopicAudit(ParentTopic parentTopic) {
        String auditPayload = Json.createObjectBuilder()
                .add("topicUUID", parentTopic.getUuid().toString())
                .add("displayName", parentTopic.getDisplayName())
                .build()
                .toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.CREATE_PARENT_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void deleteTopicAudit(Topic topic) {
        String auditPayload = Json.createObjectBuilder()
                .add("topicUUID", topic.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REMOVE_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void deleteParentTopicAudit(ParentTopic parentTopic) {
        String auditPayload = Json.createObjectBuilder()
                .add("topicUUID", parentTopic.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REMOVE_PARENT_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void reactivateTopicAudit(Topic topic) {
        String auditPayload = Json.createObjectBuilder()
                .add("topicUUID", topic.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REACTIVATE_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void reactivateParentTopicAudit(ParentTopic parentTopic) {
        String auditPayload = Json.createObjectBuilder()
                .add("topicUUID", parentTopic.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REACTIVATE_PARENT_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void updateTopicParentAudit(Topic topic) {
        String auditPayload = Json.createObjectBuilder()
                .add("topicUUID", topic.getUuid().toString())
                .add("newParentTopicUUID", topic.getParentTopic().toString())
                .build()
                .toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.UPDATE_TOPIC_PARENT.toString());
        sendAuditMessage(request);
    }

    public void addTeamToTopicAudit(TeamLink teamLink) {
        String auditPayload = Json.createObjectBuilder()
                .add("topicUUID", teamLink.getLinkUUID().toString())
                .add("teamUUID", teamLink.getResponsibleTeamUUID().toString())
                .add("caseType", teamLink.getCaseType())
                .add("stageType", teamLink.getStageType())
                .build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.ADD_TEAM_TO_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void updateTeamForTopicAudit(TeamLink teamLink, UUID oldTeamUUID) {
        String auditPayload = Json.createObjectBuilder()
                .add("topicUUID", teamLink.getLinkUUID().toString())
                .add("teamUUID", teamLink.getResponsibleTeamUUID().toString())
                .add("caseType", teamLink.getCaseType())
                .add("stageType", teamLink.getStageType())
                .add("oldTeamUUID", oldTeamUUID.toString())
                .build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.UPDATE_TEAM_FOR_TOPIC.toString());
        sendAuditMessage(request);
    }


    public void createCorrespondentType(CorrespondentType correspondentType) {
        String auditPayload = Json.createObjectBuilder()
                .add("correspondentTypeUUID", correspondentType.getUuid().toString())
                .add("displayName", correspondentType.getDisplayName())
                .add("type", correspondentType.getType())
                .build()
                .toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.CREATE_CORRESPONDENT_TYPE.toString());
        sendAuditMessage(request);
    }

    private void sendAuditMessage(CreateAuditRequest request) {

        try {
            Map<String, Object> queueHeaders = getQueueHeaders(request.getType());
            producerTemplate.sendBodyAndHeaders(auditQueue, objectMapper.writeValueAsString(request), queueHeaders);
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

    private Map<String, Object> getQueueHeaders(String eventType) {
        Map<String, Object> headers = new HashMap<>();
        headers.put(EVENT_TYPE_HEADER, eventType);
        headers.put(RequestData.CORRELATION_ID_HEADER, requestData.correlationId());
        headers.put(RequestData.USER_ID_HEADER, requestData.userId());
        headers.put(RequestData.USERNAME_HEADER, requestData.username());
        headers.put(RequestData.GROUP_HEADER, requestData.groups());
        return headers;
    }
}