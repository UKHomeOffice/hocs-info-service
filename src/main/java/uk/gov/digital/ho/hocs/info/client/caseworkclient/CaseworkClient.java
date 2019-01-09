package uk.gov.digital.ho.hocs.info.client.caseworkclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import uk.gov.digital.ho.hocs.info.application.RestHelper;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.dto.GetCaseworkCaseDataResponse;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;


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

}
