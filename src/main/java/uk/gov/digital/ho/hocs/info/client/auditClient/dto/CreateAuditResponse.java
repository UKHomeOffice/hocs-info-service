package uk.gov.digital.ho.hocs.info.client.auditClient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CreateAuditResponse {

    @JsonProperty("uuid")
    private final UUID uuid;

    @JsonProperty("correlation_id")
    private String correlationID;

    @JsonProperty("user_id")
    private String userID;

}