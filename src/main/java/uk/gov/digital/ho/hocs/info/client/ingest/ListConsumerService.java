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

    private final String HOUSE_LORDS = "lords/";
    private final String HOUSE_COMMONS = "commons/";
    private final String API_UK_PARLIAMENT;
    private final String API_SCOTTISH_PARLIAMENT;
    private final String API_NORTHERN_IRISH_ASSEMBLY;
    private final String API_EUROPEAN_PARLIAMENT;
    private final String API_WELSH_ASSEMBLY;
    private final String API_COUNTRY_REGISTER;
    private final String API_TERRITORY_REGISTER;

    private HouseAddressRepository houseAddressRepository;
    private RestTemplate restTemplate;

    @Autowired
    public ListConsumerService(@Value("${api.uk.parliament}") String apiUkParliament,
                               @Value("${api.scottish.parliament}") String apiScottishParliament,
                               @Value("${api.ni.assembly}") String apiNorthernIrishAssembly,
                               @Value("${api.european.parliament}") String apiEuropeanParliament,
                               @Value("${api.welsh.assembly}") String apiWelshAssembly,
                               @Value("${api.country.register}") String apiCountryRegister,
                               @Value("${api.territory.register}") String apiTerritoryRegister,
                               HouseAddressRepository houseAddressRepository, RestTemplate restTemplate) {
        this.API_UK_PARLIAMENT = apiUkParliament;
        this.API_SCOTTISH_PARLIAMENT = apiScottishParliament;
        this.API_NORTHERN_IRISH_ASSEMBLY = apiNorthernIrishAssembly;
        this.API_EUROPEAN_PARLIAMENT = apiEuropeanParliament;
        this.API_WELSH_ASSEMBLY = apiWelshAssembly;
        this.API_COUNTRY_REGISTER = apiCountryRegister;
        this.API_TERRITORY_REGISTER = apiTerritoryRegister;
        this.houseAddressRepository = houseAddressRepository;
        this.restTemplate = restTemplate;

    }

    public  Set<Member> createFromEuropeanParliamentAPI() {
        log.info("Updating European Parliament");
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode("EU");
        EuropeMembers europeMembers = getDataFromAPI(API_EUROPEAN_PARLIAMENT, MediaType.APPLICATION_XML, EuropeMembers.class);
        Set<Member> members = europeMembers.getMembers().stream().map(m -> new Member(House.HOUSE_EUROPEAN_PARLIAMENT.getDisplayValue(), m.getName()+" MEP", houseAddress.getUuid(),"EU"+m.getId())).collect(Collectors.toSet());
        return members;
    }

    public Set<Member> createFromIrishAssemblyAPI() {
        log.info("Updating Irish Assembly");
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode("NI");
        IrishMembers irishMembers = getDataFromAPI(API_NORTHERN_IRISH_ASSEMBLY, MediaType.APPLICATION_XML, IrishMembers.class);
        Set<Member> members = irishMembers.getMembers().stream().map(m -> new Member(House.HOUSE_NORTHERN_IRISH_ASSEMBLY.getDisplayValue(), m.getFullDisplayName()+" MLA",houseAddress.getUuid(), "NI"+m.getPersonId())).collect(Collectors.toSet());
        return members;
    }

    public Set<Member> createFromScottishParliamentAPI() {
        log.info("Updating Scottish Parliament");
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode("SP");
       ScottishMember[] scottishMembers = getDataFromAPI(API_SCOTTISH_PARLIAMENT, MediaType.APPLICATION_JSON, ScottishMember[].class);
        Set<Member> members = Arrays.stream(scottishMembers).map(m -> new Member(House.HOUSE_SCOTTISH_PARLIAMENT.getDisplayValue(), m.getName()+" MSP", houseAddress.getUuid(),"SC"+m.getPersonId())).collect(Collectors.toSet());
        return members;
    }

    public Set<Member> createCommonsFromUKParliamentAPI() {
        log.info("Updating House of Commons");
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode("HC");
        UKMembers ukUKMembers = getDataFromAPI(getFormattedUkEndpoint(HOUSE_COMMONS), MediaType.APPLICATION_XML, UKMembers.class);
        Set<Member> members = ukUKMembers.getMembers().stream().map(m -> new Member(House.HOUSE_COMMONS.getDisplayValue(),m.getFullTitle(), houseAddress.getUuid(),"CO"+m.getMemberId())).collect(Collectors.toSet());
        return members;
    }

    public Set<Member> createLordsFromUKParliamentAPI() {
        log.info("Updating House of Lords");
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode("HL");
        UKMembers ukUKMembers = getDataFromAPI(getFormattedUkEndpoint(HOUSE_LORDS), MediaType.APPLICATION_XML, UKMembers.class);
        Set<Member> members = ukUKMembers.getMembers().stream().map(m -> new Member(House.HOUSE_LORDS.getDisplayValue(), m.getFullTitle(), houseAddress.getUuid(),"LO"+m.getMemberId())).collect(Collectors.toSet());
        return members;
    }

    public Set<Member> createFromWelshAssemblyAPI() {
        log.info("Updating Welsh Assembly");
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode("WA");
        WelshWards welshWards = getDataFromAPI(API_WELSH_ASSEMBLY, MediaType.APPLICATION_XML, WelshWards.class);
        Set<WelshMembers> welshMembers = welshWards.getWards().stream().map(WelshWard::getMembers).collect(Collectors.toSet());
        Set<WelshMember> welshMemberSet = welshMembers.stream().map(WelshMembers::getMembers).flatMap(Collection::stream).collect(Collectors.toSet());
        Set<Member> members = welshMemberSet.stream().map(m -> new Member(House.HOUSE_WELSH_ASSEMBLY.getDisplayValue(), m.getName(), houseAddress.getUuid(),"WE"+m.getId())).collect(Collectors.toSet());
        return members;
    }

    public Set<Country> createFromCountryRegisterAPI() {
        log.info("Updating Countries");
        HashMap<String, HashMap<String, ArrayList<HashMap<String, String>>>> hashMap = getDataFromAPI(API_COUNTRY_REGISTER, MediaType.APPLICATION_JSON, HashMap.class);
        Set<Country> countrys = hashMap.values().stream().map(c -> new Country(c.get("item").get(0).get("name"), false)).collect(Collectors.toSet());
        return countrys;
    }

    public Set<Country> createFromTerritoryRegisterAPI() {
        log.info("Updating Territories");
        HashMap<String, HashMap<String, ArrayList<HashMap<String, String>>>> hashMap = getDataFromAPI(API_TERRITORY_REGISTER, MediaType.APPLICATION_JSON, HashMap.class);
        Set<Country> territorys = hashMap.values().stream().map(c -> new Country(c.get("item").get(0).get("name"), true)).collect(Collectors.toSet());
        return territorys;
    }

    private <T> T getDataFromAPI(String apiEndpoint, MediaType mediaType, Class<T> returnClass) {
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
