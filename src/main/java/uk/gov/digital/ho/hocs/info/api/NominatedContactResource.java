package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.CreateNominatedContactDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateNominatedContactResponse;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateNominatedContactDto;
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
    public ResponseEntity<Set<NominatedContact>> getNominatedContacts(@PathVariable UUID teamUUID) {
        Set<NominatedContact> contacts = nominatedContactService.getNominatedContacts(teamUUID);
        return ResponseEntity.ok(contacts);
    }

    @PostMapping(value = "/team/{teamUUID}/contact", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CreateNominatedContactResponse> createNominatedContact(@PathVariable UUID teamUUID, @RequestBody CreateNominatedContactDto createNominatedContactDto){
        NominatedContact nominatedContact = nominatedContactService.createNominatedContact(teamUUID, createNominatedContactDto.getEmailAddress());
        CreateNominatedContactResponse response = new CreateNominatedContactResponse(nominatedContact.getUuid().toString());
        return ResponseEntity.ok(response);
    }

    @PutMapping(value="/team/{teamUUID}/contact/{nominatedContactUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity updateNominatedContact(@PathVariable UUID nominatedContactUUID, @RequestBody UpdateNominatedContactDto updateNominatedContactDto){
        nominatedContactService.updateNominatedContact(nominatedContactUUID, updateNominatedContactDto.getEmailAddress());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value="/team/{teamUUID}/contact/{nominatedContactUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity deleteNominatedContact(@PathVariable UUID teamUUID, @PathVariable UUID nominatedContactUUID){
        nominatedContactService.deleteNominatedContact(teamUUID, nominatedContactUUID);
        return ResponseEntity.ok().build();
    }
}
