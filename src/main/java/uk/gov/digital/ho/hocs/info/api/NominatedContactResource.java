package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.api.dto.GetNominatedContactResponse;
import uk.gov.digital.ho.hocs.info.domain.model.NominatedContact;

import java.util.Set;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class NominatedContactResource {

    private final NominatedContactService nominatedContactService;

    @Autowired
    public NominatedContactResource(NominatedContactService nominatedContactService) {
        this.nominatedContactService = nominatedContactService;
    }

    @GetMapping(value = "/team/{teamUUID}/contact", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetNominatedContactResponse> getNominatedContact(@PathVariable UUID teamUUID) {
        Set<NominatedContact> contact = nominatedContactService.getNominatedContact(teamUUID);
        return ResponseEntity.ok(GetNominatedContactResponse.from(contact));
    }
}
