package uk.gov.digital.ho.hocs.info.client.caseworkclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import uk.gov.digital.ho.hocs.info.application.RestHelper;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.dto.GetCaseworkCaseDataResponse;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.dto.GetTopicResponse;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
public class CaseworkClient {


    private final RestHelper restHelper;
    private final String serviceBaseURL;

    @Autowired
    public CaseworkClient(RestHelper restHelper,
                          @Value("${hocs.case-service}") String caseService) {
        this.restHelper = restHelper;

        this.serviceBaseURL = caseService;
    }


    // TODO: don't use this, make more specific calls (more cachable)
    public GetCaseworkCaseDataResponse getCase(UUID caseUUID) {
        ResponseEntity<GetCaseworkCaseDataResponse> response = restHelper.get(serviceBaseURL, String.format("/case/%s", caseUUID), GetCaseworkCaseDataResponse.class);

        if (response.getStatusCodeValue() == 200) {
            log.info("Got Input for Case: {}", caseUUID);
            return response.getBody();
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Could not get Input; response: %s", response.getStatusCodeValue());
        }
    }

    public String getStageTypeFromStage(UUID caseUUID, UUID stageUUID) {
        ResponseEntity<String> response = restHelper.get(serviceBaseURL, String.format("/case/%s/stage/%s/type", caseUUID, stageUUID), String.class);

        if (response.getStatusCodeValue() == 200) {
            log.info("Got Type for stage: {}", stageUUID);
            return response.getBody();
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Could not get Type for stage %s; response: %s", stageUUID, response.getStatusCodeValue());
        }
    }

    //TODO: this service should not contain any reference to CaseUUIDs and should not call Casework service
    public GetTopicResponse getTopic(UUID caseUUID, UUID topicUUID) {
        ResponseEntity<GetTopicResponse> response = restHelper.get(serviceBaseURL, String.format("/case/%s/topic/%s", caseUUID, topicUUID), GetTopicResponse.class);

        if (response.getStatusCodeValue() == 200) {
            log.info("Got Topic {} for Case: {}", topicUUID, caseUUID);
            return response.getBody();
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Could not get Topic %s for Case: %s response: %s", topicUUID, caseUUID, response.getStatusCodeValue());
        }
    }

    public Set getCasesForUser(UUID userUUID, UUID teamUUID) {
        ResponseEntity<Set> response = restHelper.get(serviceBaseURL, String.format("/stage/team/%s/user/%s", teamUUID, userUUID), Set.class);
        if (response.getStatusCodeValue() == 200) {
            log.info("Got cases for User: {}", userUUID);
            return response.getBody();
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Could not get cases for User %s", userUUID, response.getStatusCodeValue());
        }
    }

    public UUID getTeamUUIDFromCaseAndStage(UUID caseUUID, UUID stageUUID) {
        ResponseEntity<UUID> response = restHelper
                .get(serviceBaseURL, String.format("/case/%s/stage/%s/team", caseUUID, stageUUID), UUID.class);
        if (response.getStatusCodeValue() == 200) {
            log.info("Got teamUUID for stage: {} & case: {}", stageUUID, caseUUID);
            return response.getBody();
        } else {
            throw new ApplicationExceptions.EntityNotFoundException(
                    "Could not get teamUUID for stage: %s & case: %s",
                    stageUUID,
                    caseUUID,
                    response.getStatusCodeValue());
        }
    }

    public void clearCachedStandardLineForTopic(UUID topicUUID) {
        ResponseEntity<String> response = restHelper.post(serviceBaseURL, String.format("/topic/%s/clearCachedStandardLine", topicUUID), null, String.class);
        if (response.getStatusCodeValue() == 200) {
            log.info("Cleared cached standard line for Topic: {}", topicUUID);
        }
    }

    public void clearCachedTemplateForCaseType(String caseType) {
        ResponseEntity<String> response = restHelper.post(serviceBaseURL, String.format("/caseType/%s/clearCachedTemplate", caseType), null, String.class);
        if (response.getStatusCodeValue() == 200) {
            log.info("Cleared cached template for caseType: {}", caseType);
        }
    }
}
