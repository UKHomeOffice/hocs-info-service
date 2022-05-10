package uk.gov.digital.ho.hocs.info.client.ingest;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Country;
import uk.gov.digital.ho.hocs.info.domain.model.enums.House;
import uk.gov.digital.ho.hocs.info.domain.model.HouseAddress;
import uk.gov.digital.ho.hocs.info.domain.model.Member;
import uk.gov.digital.ho.hocs.info.domain.model.enums.HouseCodes;
import uk.gov.digital.ho.hocs.info.domain.repository.HouseAddressRepository;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.value;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.EVENT;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.MEMBERS_API_EMPTY_RECORDS;

@Service
@Slf4j
public class ListConsumerService {

    private static final String HOUSE_LORDS = "lords/";
    private static final String HOUSE_COMMONS = "commons/";
    public static final String COUNTRY_NAME_PATH = "$.*.item[0].name";

    private final String apiUkParliament;
    private final String apiScottishParliament;
    private final String apiNorthernIrishAssembly;
    private final String apiWelshAssembly;
    private final String countriesJsonFilename;
    private final String territoriesJsonFilename;
    private final HouseAddressRepository houseAddressRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public ListConsumerService(@Value("${api.uk.parliament}") String apiUkParliament,
                               @Value("${api.scottish.parliament}") String apiScottishParliament,
                               @Value("${api.ni.assembly}") String apiNorthernIrishAssembly,
                               @Value("${api.welsh.assembly}") String apiWelshAssembly,
                               @Value("${country.json.filename}") String countriesJsonFilename,
                               @Value("${territory.json.filename}") String territoriesJsonFilename,
                               HouseAddressRepository houseAddressRepository,
                               RestTemplate restTemplate) {
        this.apiUkParliament = apiUkParliament;
        this.apiScottishParliament = apiScottishParliament;
        this.apiNorthernIrishAssembly = apiNorthernIrishAssembly;
        this.apiWelshAssembly = apiWelshAssembly;
        this.countriesJsonFilename = countriesJsonFilename;
        this.territoriesJsonFilename = territoriesJsonFilename;
        this.houseAddressRepository = houseAddressRepository;
        this.restTemplate = restTemplate;
    }

    public Set<Member> createFromIrishAssemblyAPI() {
        log.info("Updating Northern Irish Assembly");
        try {
            final HouseAddress houseAddress = retrieveHouseAddress(HouseCodes.IRISH_ASSEMBLY);

            NorthernIrishMembers northernIrishMembers =
                    getDataFromAPI(apiNorthernIrishAssembly, MediaType.APPLICATION_XML, NorthernIrishMembers.class);

            if (northernIrishMembers.getMembers() != null) {
                return northernIrishMembers.getMembers()
                        .stream().map(m ->
                                new Member(House.HOUSE_NORTHERN_IRISH_ASSEMBLY.getDisplayValue(),
                                        m.getFullDisplayName() + " MLA",
                                        houseAddress.getUuid(),
                                        "NI" + m.getPersonId()))
                        .collect(Collectors.toSet());
            }
            log.info("Northern Irish members API returned an empty set", value(EVENT, MEMBERS_API_EMPTY_RECORDS));
            return Collections.emptySet();

        } catch (ApplicationExceptions.IngestException | ApplicationExceptions.EntityNotFoundException ex) {
            log.warn(ex.getMessage());
            return Collections.emptySet();
        }
    }

    public Set<Member> createFromScottishParliamentAPI() {
        log.info("Updating Scottish Parliament");
        try {
            final HouseAddress houseAddress = retrieveHouseAddress(HouseCodes.SCOTTISH_PARLIAMENT);

            ScottishMember[] scottishMembers = getDataFromAPI(apiScottishParliament, MediaType.APPLICATION_JSON, ScottishMember[].class);

            if (scottishMembers != null && scottishMembers.length > 0) {
                return Arrays.stream(scottishMembers)
                        .map(m ->
                                new Member(House.HOUSE_SCOTTISH_PARLIAMENT.getDisplayValue(),
                                        m.getName()+" MSP",
                                        houseAddress.getUuid(),
                                        "SC"+m.getPersonId()))
                        .collect(Collectors.toSet());
            }
            log.info("Scottish Parliament members API returned an empty set", value(EVENT, MEMBERS_API_EMPTY_RECORDS));
            return Collections.emptySet();
        } catch (ApplicationExceptions.IngestException | ApplicationExceptions.EntityNotFoundException ex) {
            log.warn(ex.getMessage());
            return Collections.emptySet();
        }
    }

    public Set<Member> createCommonsFromUKParliamentAPI() {
        log.info("Updating House of Commons");
        try {
            final HouseAddress houseAddress = retrieveHouseAddress(HouseCodes.HOUSE_OF_COMMONS);

            UKMembers houseOfCommomsMembers = getDataFromAPI(getFormattedUkEndpoint(HOUSE_COMMONS), MediaType.APPLICATION_XML, UKMembers.class);

            if (houseOfCommomsMembers.getMembers() != null) {
                return houseOfCommomsMembers.getMembers()
                        .stream().map(m ->
                                new Member(House.HOUSE_COMMONS.getDisplayValue(),
                                        m.getFullTitle(),
                                        houseAddress.getUuid(),
                                        "CO" + m.getMemberId()))
                        .collect(Collectors.toSet());
            }
            log.info("House of Commons members API returned an empty set", value(EVENT, MEMBERS_API_EMPTY_RECORDS));
            return Collections.emptySet();
        } catch (ApplicationExceptions.IngestException | ApplicationExceptions.EntityNotFoundException ex) {
            log.warn(ex.getMessage());
            return Collections.emptySet();
        }
    }

    public Set<Member> createLordsFromUKParliamentAPI() {
        log.info("Updating House of Lords");
        try {
            final HouseAddress houseAddress = retrieveHouseAddress(HouseCodes.HOUSE_OF_LORDS);

            UKMembers houseOfLordsMembers = getDataFromAPI(getFormattedUkEndpoint(HOUSE_LORDS), MediaType.APPLICATION_XML, UKMembers.class);

            if (houseOfLordsMembers.getMembers() != null) {
                return houseOfLordsMembers.getMembers()
                        .stream().map(m ->
                                new Member(House.HOUSE_LORDS.getDisplayValue(),
                                        m.getFullTitle(),
                                        houseAddress.getUuid(),
                                        "LO" + m.getMemberId()))
                        .collect(Collectors.toSet());
            }
            log.info("House of Lords members API returned an empty set", value(EVENT, MEMBERS_API_EMPTY_RECORDS));
            return Collections.emptySet();
        } catch (ApplicationExceptions.IngestException | ApplicationExceptions.EntityNotFoundException ex) {
            log.warn(ex.getMessage());
            return Collections.emptySet();
        }
    }

    public Set<Member> createFromWelshAssemblyAPI() {
        log.info("Updating Welsh Assembly");
        try {
            final HouseAddress houseAddress = retrieveHouseAddress(HouseCodes.WELSH_ASSEMBLY);

            WelshWards welshWards = getDataFromAPI(apiWelshAssembly, MediaType.APPLICATION_XML, WelshWards.class);

            if (welshWards.getWards() != null) {
                Set<WelshMembers> welshMembers = welshWards.getWards().stream()
                        .map(WelshWard::getMembers)
                        .collect(Collectors.toSet());

                Set<WelshMember> welshMemberSet = welshMembers.stream()
                        .map(WelshMembers::getMembers)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());

                return welshMemberSet.stream()
                        .map(m ->
                                new Member(House.HOUSE_WELSH_ASSEMBLY.getDisplayValue(),
                                        m.getName(),
                                        houseAddress.getUuid(),
                                        "WE"+m.getId()))
                        .collect(Collectors.toSet());
            }
            log.info("Welsh Assembly members API returned an empty set", value(EVENT, MEMBERS_API_EMPTY_RECORDS));
            return Collections.emptySet();
        } catch (ApplicationExceptions.IngestException | ApplicationExceptions.EntityNotFoundException ex) {
            log.warn(ex.getMessage());
            return Collections.emptySet();
        }
    }

    public Set<Country> createFromCountryFile() {
        log.info("Updating Countries from file");
        return parseLocationJson(countriesJsonFilename, true);
    }

    public Set<Country> createFromTerritoryFile() {
        log.info("Updating Territories from file");
        return parseLocationJson(territoriesJsonFilename, false);
    }

    public Set<Country> parseLocationJson(String fileName, Boolean isCountry) {
        InputStream jsonStream = inputStreamFromClasspath(fileName);
        DocumentContext ctx = JsonPath.parse(jsonStream);
        List<String> territoryStrings = ctx.read(COUNTRY_NAME_PATH);
        return territoryStrings
                .stream()
                .map(name -> new Country(name, isCountry))
                .collect(Collectors.toSet());
    }

    private static InputStream inputStreamFromClasspath(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    private <T> T getDataFromAPI(String apiEndpoint, MediaType mediaType, Class<T> returnClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(mediaType));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<T> response = null;
        try {
            log.info("Attempting to hit endpoint {}, returning type {} of class {}", apiEndpoint, mediaType, returnClass.getName());
            response = restTemplate.exchange(apiEndpoint, HttpMethod.GET, entity, returnClass);
            log.info("Response to endpoint {} is {}", apiEndpoint, response);
        } catch (Exception e) {
            log.info("exchange call exception : " + e.getMessage());
            throw new ApplicationExceptions.IngestException("ListConsumerService exchange exception : " + e.getMessage() + " endpoint : " +
                    apiEndpoint + " headers : " + headers.toString() + " media type : " +  mediaType.toString());
        }
        return response.getBody();
    }

    private String getFormattedUkEndpoint(@NonNull final String house) {
        return String.format(apiUkParliament, house);
    }

    private HouseAddress retrieveHouseAddress(@NonNull final HouseCodes houseCode) {
        HouseAddress houseAddress = houseAddressRepository.findByHouseCode(houseCode.getHouseCode());

        if (houseAddress == null) {
            throw new ApplicationExceptions.EntityNotFoundException(
                    String.format("House address for code %s could not be found.", houseCode.getHouseCode()));
        }
        return houseAddress;
    }
}
