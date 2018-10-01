package uk.gov.digital.ho.hocs.info.memberAddress;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.dto.GetMembersAddressResponse;
import uk.gov.digital.ho.hocs.info.entities.MemberHouseAddress;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
public class MemberAddressResource {

    private final MemberAddressService memberAddressService;

    @Autowired
    public MemberAddressResource(MemberAddressService memberAddressService) {
        this.memberAddressService = memberAddressService;
    }

    @GetMapping(value = "/member/{uuid}/address", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetMembersAddressResponse> getMemberAddressByUUID(@PathVariable UUID uuid) {
        log.info("requesting house address for member {}", uuid);
        MemberHouseAddress memberHouseAddress = memberAddressService.getMemberAndAddress(uuid);
        return ResponseEntity.ok(GetMembersAddressResponse.from(memberHouseAddress));

    }
}