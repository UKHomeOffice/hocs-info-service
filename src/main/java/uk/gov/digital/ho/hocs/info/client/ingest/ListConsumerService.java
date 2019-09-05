package uk.gov.digital.ho.hocs.info.client.ingest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Constituency;
import uk.gov.digital.ho.hocs.info.domain.model.House;
import uk.gov.digital.ho.hocs.info.domain.model.HouseAddress;
import uk.gov.digital.ho.hocs.info.domain.model.Member;
import uk.gov.digital.ho.hocs.info.domain.repository.ConstituencyRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.HouseAddressRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ListConsumerService {

    private final String HOUSE_LORDS = "lords/";
    private final String HOUSE_COMMONS = "commons/";
    private final String API_UK_PARLIAMENT;
    private final String API_SCOTTISH_PARLIAMENT;
    private final String API_NORTHERN_IRISH_ASSEMBLY;
    private final String API_EUROPEAN_PARLIAMENT;
    private final String API_WELSH_ASSEMBLY;
    private final String API_UK_CONSTITUENCIES;

    private ConstituencyRepository constituencyRepository;
    private HouseAddressRepository houseAddressRepository;
    private RestTemplate restTemplate;

    @Autowired
    public ListConsumerService(@Value("${api.uk.parliament}") String apiUkParliament,
                               @Value("${api.scottish.parliament}") String apiScottishParliament,
                               @Value("${api.ni.assembly}") String apiNorthernIrishAssembly,
                               @Value("${api.european.parliament}") String apiEuropeanParliament,
                               @Value("${api.welsh.assembly}") String apiWelshAssembly,
                               ConstituencyRepository constituencyRepository,
                               HouseAddressRepository houseAddressRepository, RestTemplate restTemplate) {
        this.API_UK_PARLIAMENT = apiUkParliament;
        this.API_SCOTTISH_PARLIAMENT = apiScottishParliament;
        this.API_NORTHERN_IRISH_ASSEMBLY = apiNorthernIrishAssembly;
        this.API_EUROPEAN_PARLIAMENT = apiEuropeanParliament;
        this.API_WELSH_ASSEMBLY = apiWelshAssembly;
        this.API_UK_CONSTITUENCIES = getFormattedUkEndpoint(HOUSE_COMMONS);
        this.constituencyRepository = constituencyRepository;
        this.houseAddressRepository = houseAddressRepository;
        this.restTemplate = restTemplate;
    }

    public Set<Constituency> createUKConstituencyFromUKParliamentAPI() {
        log.info("Updating constituencies");
        UKConstituencys ukConstituencys = getMembersFromAPI(API_UK_CONSTITUENCIES, MediaType.APPLICATION_XML, UKConstituencys.class);
        Set<Constituency> constituencys = ukConstituencys.getConstituencys().stream().map(c -> new Constituency(c.getConstituency())).collect(Collectors.toSet());
        return constituencys;
    }

    public  Set<Member> createFromEuropeanParliamentAPI() {
        log.info("Updating European Parliament");
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode("EU");
        EuropeMembers europeMembers = getMembersFromAPI(API_EUROPEAN_PARLIAMENT, MediaType.APPLICATION_XML, EuropeMembers.class);
        Set<Member> members = europeMembers.getMembers().stream().map(m -> new Member(House.HOUSE_EUROPEAN_PARLIAMENT.getDisplayValue(), m.getName()+" MEP", houseAddress.getUuid(),"EU"+m.getId())).collect(Collectors.toSet());
        return members;
    }

    public Set<Member> createFromIrishAssemblyAPI() {
        log.info("Updating Irish Assembly");
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode("NI");
        IrishMembers irishMembers = getMembersFromAPI(API_NORTHERN_IRISH_ASSEMBLY, MediaType.APPLICATION_XML, IrishMembers.class);
        Set<Member> members = irishMembers.getMembers().stream().map(m -> new Member(House.HOUSE_NORTHERN_IRISH_ASSEMBLY.getDisplayValue(), m.getFullDisplayName()+" MLA",houseAddress.getUuid(), "NI"+m.getPersonId())).collect(Collectors.toSet());
        return members;
    }

    public Set<Member> createFromScottishParliamentAPI() {
        log.info("Updating Scottish Parliament");
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode("SP");
       ScottishMember[] scottishMembers = getMembersFromAPI(API_SCOTTISH_PARLIAMENT, MediaType.APPLICATION_JSON, ScottishMember[].class);
        Set<Member> members = Arrays.stream(scottishMembers).map(m -> new Member(House.HOUSE_SCOTTISH_PARLIAMENT.getDisplayValue(), m.getName()+" MSP", houseAddress.getUuid(),"SC"+m.getPersonId())).collect(Collectors.toSet());
        return members;
    }

    public Set<Member> createCommonsFromUKParliamentAPI() {
        log.info("Updating House of Commons");
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode("HC");
        UKMembers ukUKMembers = getMembersFromAPI(getFormattedUkEndpoint(HOUSE_COMMONS), MediaType.APPLICATION_XML, UKMembers.class);
        Set<Member> members = ukUKMembers.getMembers().stream().map(m -> new Member(House.HOUSE_COMMONS.getDisplayValue(),m.getFullTitle(), houseAddress.getUuid(),"CO"+m.getMemberId(), constituencyRepository.findActiveConstituencyByName(m.getMemberFrom()).getUuid(), m.getMemberFrom())).collect(Collectors.toSet());
        return members;
    }

    public Set<Member> createLordsFromUKParliamentAPI() {
        log.info("Updating House of Lords");
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode("HL");
        UKMembers ukUKMembers = getMembersFromAPI(getFormattedUkEndpoint(HOUSE_LORDS), MediaType.APPLICATION_XML, UKMembers.class);
        Set<Member> members = ukUKMembers.getMembers().stream().map(m -> new Member(House.HOUSE_LORDS.getDisplayValue(), m.getFullTitle(), houseAddress.getUuid(),"LO"+m.getMemberId())).collect(Collectors.toSet());
        return members;
    }

    public Set<Member> createFromWelshAssemblyAPI() {
        log.info("Updating Welsh Assembly");
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode("WA");
        WelshWards welshWards = getMembersFromAPI(API_WELSH_ASSEMBLY, MediaType.APPLICATION_XML, WelshWards.class);
        Set<WelshMembers> welshMembers = welshWards.getWards().stream().map(WelshWard::getMembers).collect(Collectors.toSet());
        Set<WelshMember> welshMemberSet = welshMembers.stream().map(WelshMembers::getMembers).flatMap(Collection::stream).collect(Collectors.toSet());
        Set<Member> members = welshMemberSet.stream().map(m -> new Member(House.HOUSE_WELSH_ASSEMBLY.getDisplayValue(), m.getName(), houseAddress.getUuid(),"WE"+m.getId())).collect(Collectors.toSet());
        return members;
    }

    private <T> T getMembersFromAPI(String apiEndpoint, MediaType mediaType, Class<T> returnClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(mediaType));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<T> response = restTemplate.exchange(apiEndpoint, HttpMethod.GET, entity, returnClass);

        if (response == null || response.getStatusCodeValue() != 200) {
            throw new ApplicationExceptions.IngestException("members Not Found at " + apiEndpoint);
        }
        return response.getBody();
    }

    private String getFormattedUkEndpoint(final String house) {
        return String.format(API_UK_PARLIAMENT, house);
    }

}
