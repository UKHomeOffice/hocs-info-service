package uk.gov.digital.ho.hocs.info.client.audit.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CreateAuditRequest {

    @JsonProperty(value= "correlation_id", required = true)
    private String correlationID;

    @JsonProperty(value= "raising_service", required = true)
    private String raisingService;

    @JsonProperty(value= "audit_payload")
    private String auditPayload;

    @JsonProperty(value= "namespace", required = true)
    private String namespace;

    @JsonProperty(value="audit_timestamp", required = true)
    private LocalDateTime auditTimestamp;

    @JsonProperty(value= "type", required = true)
    private String type;

    @JsonProperty(value= "user_id", required = true)
    private String userID;

}

