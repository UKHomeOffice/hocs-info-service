package uk.gov.digital.ho.hocs.info.memberAddress;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.entities.MemberHouseAddress;
import uk.gov.digital.ho.hocs.info.repositories.MemberAddressRepository;

import java.util.UUID;

@Service
@Slf4j
public class MemberAddressService {

    private final MemberAddressRepository memberAddressRepository;


    @Autowired
    public MemberAddressService(MemberAddressRepository memberAddressRepository) {
        this.memberAddressRepository = memberAddressRepository;

    }

    public MemberHouseAddress getMemberAndAddress(UUID uuid) {
        log.debug("Requesting House Address for Member {}", uuid);
        return memberAddressRepository.findMemberAndAddressByUUID(uuid);
    }

}
