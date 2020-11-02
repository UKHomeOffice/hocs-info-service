package uk.gov.digital.ho.hocs.info.client.ingest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Country;
import uk.gov.digital.ho.hocs.info.domain.model.House;
import uk.gov.digital.ho.hocs.info.domain.model.HouseAddress;
import uk.gov.digital.ho.hocs.info.domain.model.Member;
import uk.gov.digital.ho.hocs.info.domain.repository.HouseAddressRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ListConsumerService {

    private static final String HOUSE_LORDS = "lords/";
    private static final String HOUSE_COMMONS = "commons/";
    private final String apiUkParliament;
    private final String apiScottishParliament;
    private final String apiNorthernIrishAssembly;
    private final String apiWelshAssembly;
    private final String apiCountryRegister;
    private final String apiTerritoryRegister;

    private HouseAddressRepository houseAddressRepository;
    private RestTemplate restTemplate;

    @Autowired
    public ListConsumerService(@Value("${api.uk.parliament}") String apiUkParliament,
                               @Value("${api.scottish.parliament}") String apiScottishParliament,
                               @Value("${api.ni.assembly}") String apiNorthernIrishAssembly,
                               @Value("${api.welsh.assembly}") String apiWelshAssembly,
                               @Value("${api.country.register}") String apiCountryRegister,
                               @Value("${api.territory.register}") String apiTerritoryRegister,
                               HouseAddressRepository houseAddressRepository, RestTemplate restTemplate) {
        this.apiUkParliament = apiUkParliament;
        this.apiScottishParliament = apiScottishParliament;
        this.apiNorthernIrishAssembly = apiNorthernIrishAssembly;
        this.apiWelshAssembly = apiWelshAssembly;
        this.apiCountryRegister = apiCountryRegister;
        this.apiTerritoryRegister = apiTerritoryRegister;
        this.houseAddressRepository = houseAddressRepository;
        this.restTemplate = restTemplate;

    }

    public Set<Member> createFromIrishAssemblyAPI() {
        log.info("Updating Irish Assembly");
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode("NI");
        IrishMembers irishMembers = getDataFromAPI(apiNorthernIrishAssembly, MediaType.APPLICATION_XML, IrishMembers.class);
        return irishMembers.getMembers().stream().map(m -> new Member(House.HOUSE_NORTHERN_IRISH_ASSEMBLY.getDisplayValue(), m.getFullDisplayName()+" MLA",houseAddress.getUuid(), "NI"+m.getPersonId())).collect(Collectors.toSet());
    }

    public Set<Member> createFromScottishParliamentAPI() {
        log.info("Updating Scottish Parliament");
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode("SP");
       ScottishMember[] scottishMembers = getDataFromAPI(apiScottishParliament, MediaType.APPLICATION_JSON, ScottishMember[].class);
        return Arrays.stream(scottishMembers).map(m -> new Member(House.HOUSE_SCOTTISH_PARLIAMENT.getDisplayValue(), m.getName()+" MSP", houseAddress.getUuid(),"SC"+m.getPersonId())).collect(Collectors.toSet());
    }

    public Set<Member> createCommonsFromUKParliamentAPI() {
        log.info("Updating House of Commons");
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode("HC");
        UKMembers ukUKMembers = getDataFromAPI(getFormattedUkEndpoint(HOUSE_COMMONS), MediaType.APPLICATION_XML, UKMembers.class);
        return ukUKMembers.getMembers().stream().map(m -> new Member(House.HOUSE_COMMONS.getDisplayValue(),m.getFullTitle(), houseAddress.getUuid(),"CO"+m.getMemberId())).collect(Collectors.toSet());
    }

    public Set<Member> createLordsFromUKParliamentAPI() {
        log.info("Updating House of Lords");
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode("HL");
        UKMembers ukUKMembers = getDataFromAPI(getFormattedUkEndpoint(HOUSE_LORDS), MediaType.APPLICATION_XML, UKMembers.class);
        return ukUKMembers.getMembers().stream().map(m -> new Member(House.HOUSE_LORDS.getDisplayValue(), m.getFullTitle(), houseAddress.getUuid(),"LO"+m.getMemberId())).collect(Collectors.toSet());
    }

    public Set<Member> createFromWelshAssemblyAPI() {
        log.info("Updating Welsh Assembly");
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode("WA");
        WelshWards welshWards = getDataFromAPI(apiWelshAssembly, MediaType.APPLICATION_XML, WelshWards.class);
        Set<WelshMembers> welshMembers = welshWards.getWards().stream().map(WelshWard::getMembers).collect(Collectors.toSet());
        Set<WelshMember> welshMemberSet = welshMembers.stream().map(WelshMembers::getMembers).flatMap(Collection::stream).collect(Collectors.toSet());
        return welshMemberSet.stream().map(m -> new Member(House.HOUSE_WELSH_ASSEMBLY.getDisplayValue(), m.getName(), houseAddress.getUuid(),"WE"+m.getId())).collect(Collectors.toSet());
    }

    public Set<Country> createFromCountryRegisterAPI() {
        log.info("Updating Countries");
        HashMap<String, HashMap<String, ArrayList<HashMap<String, String>>>> hashMap = getDataFromAPI(apiCountryRegister, MediaType.APPLICATION_JSON, HashMap.class);
        return hashMap.values().stream().map(c -> new Country(c.get("item").get(0).get("name"), false)).collect(Collectors.toSet());
    }

    public Set<Country> createFromTerritoryRegisterAPI() {
        log.info("Updating Territories");
        HashMap<String, HashMap<String, ArrayList<HashMap<String, String>>>> hashMap = getDataFromAPI(apiTerritoryRegister, MediaType.APPLICATION_JSON, HashMap.class);
        return hashMap.values().stream().map(c -> new Country(c.get("item").get(0).get("name"), true)).collect(Collectors.toSet());
    }

    private <T> T getDataFromAPI(String apiEndpoint, MediaType mediaType, Class<T> returnClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(mediaType));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<T> response = null;

        try {
            response = restTemplate.exchange(apiEndpoint, HttpMethod.GET, entity, returnClass);
        } catch (Exception e) {
            throw new ApplicationExceptions.IngestException("Members Not Found at " + apiEndpoint);
        }

        return response.getBody();
    }

    private String getFormattedUkEndpoint(final String house) {
        return String.format(apiUkParliament, house);
    }

}
