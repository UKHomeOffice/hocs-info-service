package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.*;
import uk.gov.digital.ho.hocs.info.domain.model.Constituency;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
public class ConstituencyResource {

    private final ConstituencyService constituencyService;

    @Autowired
    public ConstituencyResource(ConstituencyService constituencyService) {
        this.constituencyService = constituencyService;
    }

    @GetMapping(value = "/case/{caseUUID}/constituencylist", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetAllConstituencysResponse> getAllConstituencys(@PathVariable UUID caseUUID) {
        List<Constituency> constituencys = constituencyService.getConstituencyList(caseUUID);
        return ResponseEntity.ok(GetAllConstituencysResponse.from(constituencys));
    }

    @GetMapping(value = "/constituencys/{caseType}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetAllConstituencysResponse> getAllConstituencysByCaseType(@PathVariable String caseType) {
        log.info("requesting all constituencys for case type {}", caseType);
        List<Constituency> constituencys = constituencyService.getConstituencyList(caseType);
        return ResponseEntity.ok(GetAllConstituencysResponse.from(constituencys));
    }

    @GetMapping(value = "/constituency/{constituencyUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ConstituencyDto> getConstituencyByUUID(@PathVariable UUID constituencyUUID) {
        log.info("requesting constituency {}", constituencyUUID);
        Constituency constituency = constituencyService.getConstituency(constituencyUUID);
        return ResponseEntity.ok(ConstituencyDto.from(constituency));
    }

    @GetMapping(value = "/constituency/member/{externalReference}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ConstituencyDto> getConstituencyByMemberExternalReference(@PathVariable String externalReference) {
        log.info("requesting constituency by member externalReference {}", externalReference);
        Constituency constituency = constituencyService.getConstituencyByMemberExternalReference(externalReference);
        return ResponseEntity.ok(ConstituencyDto.from(constituency));
    }

    @GetMapping(value = "/constituencys", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<ConstituencyDto>> getConstituencys () {
        log.info("requesting all constituencys");
        List<Constituency> constituencys = constituencyService.getConstituencys();
        return ResponseEntity.ok(constituencys.stream().map(t->ConstituencyDto.from(t)).collect(Collectors.toSet()));
    }

    @PostMapping(value = "/constituency", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CreateConstituencyResponse> createConstituency(@RequestBody CreateConstituencyDto request) {
        log.info("Creating new constituency {}", request.getConstituencyName());
        UUID constituencyUUID = constituencyService.createConstituency(request);
        return ResponseEntity.ok(new CreateConstituencyResponse(constituencyUUID.toString()));
    }

    @DeleteMapping(value = "/constituency/{constituencyUUID}")
    public ResponseEntity deleteConstituency(@PathVariable UUID constituencyUUID) {
        constituencyService.deleteConstituency(constituencyUUID);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/constituency/{constituencyUUID}")
    public ResponseEntity reactivateConstituency(@PathVariable UUID constituencyUUID){
        constituencyService.reactivateConstituency(constituencyUUID);
        return ResponseEntity.ok().build();
    }
}
