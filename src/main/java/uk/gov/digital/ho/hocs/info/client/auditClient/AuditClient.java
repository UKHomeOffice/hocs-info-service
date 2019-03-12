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
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import java.time.LocalDateTime;
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

    public void createTeamAudit(Team team) {
        String auditPayload = Json.createObjectBuilder().add("teamUUID", team.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.CREATE_TEAM.toString());

        try {
            producerTemplate.sendBody(auditQueue, objectMapper.writeValueAsString(request));
            log.info("Create audit for Create Team, team UUID: {}, correlationID: {}, UserID: {}", team.getUuid(), requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create audit event for team UUID {} for reason {}", team.getUuid(), e, value(EVENT, AUDIT_FAILED));
        }
    }

    public void renameTeamAudit(Team team) {
        String auditPayload = Json.createObjectBuilder().add("teamUUID", team.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.RENAME_TEAM.toString());

        try {
            producerTemplate.sendBody(auditQueue, objectMapper.writeValueAsString(request));
            log.info("Create audit for Rename Team, team UUID: {}, correlationID: {}, UserID: {}", team.getUuid(), requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create audit event for team UUID {} for reason {}", team.getUuid(), e, value(EVENT, AUDIT_FAILED));
        }
    }

    public void deleteTeamAudit(Team team) {
        String auditPayload = Json.createObjectBuilder().add("teamUUID", team.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.DELETE_TEAM.toString());

        try {
            producerTemplate.sendBody(auditQueue, objectMapper.writeValueAsString(request));
            log.info("Create audit for Delete Team, team UUID: {}, correlationID: {}, UserID: {}", team.getUuid(), requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create Delete Team audit event for team UUID {} for reason {}", team.getUuid(), e, value(EVENT, AUDIT_FAILED));
        }
    }

    public void addUserToTeamAudit(UUID userUUID, Team team) {
        String auditPayload = Json.createObjectBuilder().add("teamUUID", team.getUuid().toString()).add("userUUID", userUUID.toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.ADD_USER_TO_TEAM.toString());

        try {
            producerTemplate.sendBody(auditQueue, objectMapper.writeValueAsString(request));
            log.info("Create audit for Add User to Team, team UUID: {}, added user UUID: {}, correlationID: {}, UserID: {}", team.getUuid(), userUUID, requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create Add User to Team audit event for team UUID {} for reason {}", team.getUuid(), e, value(EVENT, AUDIT_FAILED));
        }
    }


    public void moveToNewUnitAudit(String teamUUID, String oldUnitUUID, String newUnitUUID) {
        String auditPayload = Json.createObjectBuilder().add("teamUUID", teamUUID).add("oldUnit", oldUnitUUID).add("newUnit", newUnitUUID).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.MOVE_TEAM_TO_UNIT.toString());

        try {
            producerTemplate.sendBody(auditQueue, objectMapper.writeValueAsString(request));
            log.info("Create audit for Move Team to New Unit, team UUID: {}, new unit UUID: {}, correlationID: {}, UserID: {}", teamUUID, newUnitUUID, requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create Move Team to New Unit audit event for team UUID {} for reason {}", teamUUID, e, value(EVENT, AUDIT_FAILED));
        }
    }

    public void updateTeamPermissionsAudit(UUID teamUUID, Set<PermissionDto> permissions) {
        for (PermissionDto permission : permissions) {
            permissionArray.add(Json.createObjectBuilder().add("caseType", permission.getCaseTypeCode()).add("accessLevel", permission.getAccessLevel().toString()).build());
        }
        String auditPayload = Json.createObjectBuilder().add("permissions", permissionArray).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.UPDATE_TEAM_PERMISSIONS.toString());

        try {
            producerTemplate.sendBody(auditQueue, objectMapper.writeValueAsString(request));
            log.info("Create audit for Update Team Permission, team UUID: {}, correlationID: {}, UserID: {}", teamUUID, requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create Update Team Permission audit event for team UUID {} for reason {}", teamUUID, e, value(EVENT, AUDIT_FAILED));
        }
    }

    public void deleteTeamPermissionsAudit(UUID teamUUID, Set<PermissionDto> permissions) {
        for (PermissionDto permission : permissions) {
            permissionArray.add(Json.createObjectBuilder().add("caseType", permission.getCaseTypeCode()).add("accessLevel", permission.getAccessLevel().toString()).build());
        }
        String auditPayload = Json.createObjectBuilder().add("permissions", permissionArray).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REMOVE_PERMISSIONS_FROM_TEAM.toString());

        try {
            producerTemplate.sendBody(auditQueue, objectMapper.writeValueAsString(request));
            log.info("Create audit for Delete Team Permission, team UUID: {}, correlationID: {}, UserID: {}", teamUUID, requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create Delete Team Permission audit event for team UUID {} for reason {}", teamUUID, e, value(EVENT, AUDIT_FAILED));
        }
    }

    public void removeUserFromTeamAudit(UUID userUUID, UUID teamUUID) {
        String auditPayload = Json.createObjectBuilder().add("teamUUID", teamUUID.toString()).add("userUUID", userUUID.toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REMOVE_USER_FROM_TEAM.toString());

        try {
            producerTemplate.sendBody(auditQueue, objectMapper.writeValueAsString(request));
            log.info("Create audit for Remove User from Team, user UUID: {}, team UUID: {}, correlationID: {}, UserID: {}",
                    userUUID, teamUUID, requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create Move Team to New Unit audit event for team UUID {} for reason {}", teamUUID, e, value(EVENT, AUDIT_FAILED));
        }
    }

    public void createTopicAudit(Topic topic) {
        String auditPayload = Json.createObjectBuilder()
                .add("topicUUID", topic.getUuid().toString())
                .add("displayName", topic.getDisplayName())
                .add("parentTopicUUID", topic.getParentTopic().toString())
                .build()
                .toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.CREATE_TOPIC.toString());

        try {
            producerTemplate.sendBody(auditQueue, objectMapper.writeValueAsString(request));
            log.info("Create audit for Create Topic, topic UUID: {}, correlationID: {}, UserID: {}", topic.getUuid(), requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create audit event for create topic UUID {} for reason {}", topic.getUuid(), e, value(EVENT, AUDIT_FAILED));
        }
    }

    public void createParentTopicAudit(ParentTopic parentTopic) {
        String auditPayload = Json.createObjectBuilder()
                .add("topicUUID", parentTopic.getUuid().toString())
                .add("displayName", parentTopic.getDisplayName())
                .build()
                .toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.CREATE_PARENT_TOPIC.toString());

        try {
            producerTemplate.sendBody(auditQueue, objectMapper.writeValueAsString(request));
            log.info("Create audit for Create Parent Topic, topic UUID: {}, correlationID: {}, UserID: {}", parentTopic.getUuid(), requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create audit event for create parent topic UUID {} for reason {}", parentTopic.getUuid(), e, value(EVENT, AUDIT_FAILED));
        }
    }

    public void deleteTopicAudit(Topic topic) {
        String auditPayload = Json.createObjectBuilder()
                .add("topicUUID", topic.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REMOVE_TOPIC.toString());

        try {
            producerTemplate.sendBody(auditQueue, objectMapper.writeValueAsString(request));
            log.info("Create audit for Delete Topic, topic UUID: {}, correlationID: {}, UserID: {}", topic.getUuid(), requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create audit event for remove topic UUID {} for reason {}", topic.getUuid(), e, value(EVENT, AUDIT_FAILED));
        }
    }

    public void deleteParentTopicAudit(ParentTopic parentTopic) {
        String auditPayload = Json.createObjectBuilder()
                .add("topicUUID", parentTopic.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REMOVE_PARENT_TOPIC.toString());

        try {
            producerTemplate.sendBody(auditQueue, objectMapper.writeValueAsString(request));
            log.info("Create audit for Delete Parent Topic, topic UUID: {}, correlationID: {}, UserID: {}", parentTopic.getUuid(), requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create audit event for create parent topic UUID {} for reason {}", parentTopic.getUuid(), e, value(EVENT, AUDIT_FAILED));
        }
    }

    public void reactivateTopicAudit(Topic topic) {
        String auditPayload = Json.createObjectBuilder()
                .add("topicUUID", topic.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REACTIVATE_TOPIC.toString());

        try {
            producerTemplate.sendBody(auditQueue, objectMapper.writeValueAsString(request));
            log.info("Create audit for Reactivate Topic, topic UUID: {}, correlationID: {}, UserID: {}", topic.getUuid(), requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create audit event for reactivate topic UUID {} for reason {}", topic.getUuid(), e, value(EVENT, AUDIT_FAILED));
        }
    }

    public void reactivateParentTopicAudit(ParentTopic parentTopic) {
        String auditPayload = Json.createObjectBuilder()
                .add("topicUUID", parentTopic.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REACTIVATE_PARENT_TOPIC.toString());

        try {
            producerTemplate.sendBody(auditQueue, objectMapper.writeValueAsString(request));
            log.info("Create audit for Reactivate Parent Topic, topic UUID: {}, correlationID: {}, UserID: {}", parentTopic.getUuid(), requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create audit event for reactivate parent topic UUID {} for reason {}", parentTopic.getUuid(), e, value(EVENT, AUDIT_FAILED));
        }
    }

    public void updateTopicDisplayNameAudit(Topic topic) {
        String auditPayload = Json.createObjectBuilder()
                .add("topicUUID", topic.getUuid().toString())
                .add("newDisplayName", topic.getDisplayName())
                .build()
                .toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.UPDATE_TOPIC_DISPLAY_NAME.toString());

        try {
            producerTemplate.sendBody(auditQueue, objectMapper.writeValueAsString(request));
            log.info("Create audit for Update Topic display name, topic UUID: {}, correlationID: {}, UserID: {}", topic.getUuid(), requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create audit event for update display name for topic UUID {} for reason {}", topic.getUuid(), e, value(EVENT, AUDIT_FAILED));
        }
    }

    public void updateTopicParentAudit(Topic topic) {
        String auditPayload = Json.createObjectBuilder()
                .add("topicUUID", topic.getUuid().toString())
                .add("newParentTopicUUID", topic.getParentTopic().toString())
                .build()
                .toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.UPDATE_TOPIC_PARENT.toString());

        try {
            producerTemplate.sendBody(auditQueue, objectMapper.writeValueAsString(request));
            log.info("Create audit for Reactivate Topic, topic UUID: {}, correlationID: {}, UserID: {}", topic.getUuid(), requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create audit event for reactivate topic UUID {} for reason {}", topic.getUuid(), e, value(EVENT, AUDIT_FAILED));
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


}