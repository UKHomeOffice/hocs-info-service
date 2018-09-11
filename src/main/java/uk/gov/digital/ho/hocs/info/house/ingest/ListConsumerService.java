package uk.gov.digital.ho.hocs.info.house.ingest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.digital.ho.hocs.info.exception.IngestException;
import uk.gov.digital.ho.hocs.info.entities.House;
import uk.gov.digital.ho.hocs.info.entities.Member;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ListConsumerService {

    private final String HOUSE_LORDS = "lords";
    private final String HOUSE_COMMONS = "commons";

    private final String API_UK_PARLIAMENT;
    private final String API_SCOTTISH_PARLIAMENT;
    private final String API_NORTHERN_IRISH_ASSEMBLY;
    private final String API_EUROPEAN_PARLIAMENT;
    private final String API_WELSH_ASSEMBLY;

    @Autowired
    public ListConsumerService(@Value("${api.uk.parliament}") String apiUkParliament,
                               @Value("${api.scottish.parliament}") String apiScottishParliament,
                               @Value("${api.ni.assembly}") String apiNorthernIrishAssembly,
                               @Value("${api.european.parliament}") String apiEuropeanParliament,
                               @Value("${api.welsh.assembly}") String apiWelshAssembly) {
        this.API_UK_PARLIAMENT = apiUkParliament;
        this.API_SCOTTISH_PARLIAMENT = apiScottishParliament;
        this.API_NORTHERN_IRISH_ASSEMBLY = apiNorthernIrishAssembly;
        this.API_EUROPEAN_PARLIAMENT = apiEuropeanParliament;
        this.API_WELSH_ASSEMBLY = apiWelshAssembly;
    }

    public  Set<Member> createFromEuropeanParliamentAPI() throws IngestException {
        log.info("Updating European Parliament");
        EuropeMembers europeMembers = getMembersFromAPI(API_EUROPEAN_PARLIAMENT, MediaType.APPLICATION_XML, EuropeMembers.class);
        Set<Member> members = europeMembers.getMembers().stream().map(m -> new Member(House.HOUSE_EUROPEAN_PARLIAMENT.getDisplayValue(), m.getName()+" MEP", "EU"+m.getId())).collect(Collectors.toSet());
        return members;
    }

    public Set<Member> createFromIrishAssemblyAPI() throws IngestException {
        log.info("Updating Irish Assembly");
        IrishMembers irishMembers = getMembersFromAPI(API_NORTHERN_IRISH_ASSEMBLY, MediaType.APPLICATION_XML, IrishMembers.class);
        Set<Member> members = irishMembers.getMembers().stream().map(m -> new Member(House.HOUSE_NORTHERN_IRISH_ASSEMBLY.getDisplayValue(), m.getFullDisplayName()+" MLA", "NI"+m.getPersonId())).collect(Collectors.toSet());
        return members;
    }

    public Set<Member> createFromScottishParliamentAPI() throws IngestException {
        log.info("Updating Scottish Parliament");
       ScottishMember[] scottishMembers = getMembersFromAPI(API_SCOTTISH_PARLIAMENT, MediaType.APPLICATION_JSON, ScottishMember[].class);
        Set<Member> members = Arrays.stream(scottishMembers).map(m -> new Member(House.HOUSE_SCOTTISH_PARLIAMENT.getDisplayValue(), m.getName()+" MSP", "SC"+m.getPersonId())).collect(Collectors.toSet());
        return members;
    }

    public Set<Member> createCommonsFromUKParliamentAPI() throws IngestException {
        log.info("Updating House of Commons");
        UKMembers ukUKMembers = getMembersFromAPI(getFormattedUkEndpoint(HOUSE_COMMONS), MediaType.APPLICATION_XML, UKMembers.class);
        Set<Member> members = ukUKMembers.getMembers().stream().map(m -> new Member(House.HOUSE_COMMONS.getDisplayValue(),m.getFullTitle(), "CO"+m.getMemberId())).collect(Collectors.toSet());
        return members;
    }

    public Set<Member> createLordsFromUKParliamentAPI() throws IngestException {
        log.info("Updating House of Lords");
        UKMembers ukUKMembers = getMembersFromAPI(getFormattedUkEndpoint(HOUSE_LORDS), MediaType.APPLICATION_XML, UKMembers.class);
        Set<Member> members = ukUKMembers.getMembers().stream().map(m -> new Member(House.HOUSE_LORDS.getDisplayValue(), m.getFullTitle(), "LO"+m.getMemberId())).collect(Collectors.toSet());
        return members;
    }

    public Set<Member> createFromWelshAssemblyAPI() throws IngestException {
        log.info("Updating Welsh Assembly");
        WelshWards welshWards = getMembersFromAPI(API_WELSH_ASSEMBLY, MediaType.APPLICATION_XML, WelshWards.class);
        Set<WelshMembers> welshMembers = welshWards.getWards().stream().map(WelshWard::getMembers).collect(Collectors.toSet());
        Set<WelshMember> welshMemberSet = welshMembers.stream().map(WelshMembers::getMembers).flatMap(Collection::stream).collect(Collectors.toSet());
        Set<Member> members = welshMemberSet.stream().map(m -> new Member(House.HOUSE_WELSH_ASSEMBLY.getDisplayValue(), m.getName(), "WE"+m.getId())).collect(Collectors.toSet());
        return members;
    }

    private <T> T getMembersFromAPI(String apiEndpoint, MediaType mediaType, Class<T> returnClass) throws IngestException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(mediaType));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<T> response = new RestTemplate().exchange(apiEndpoint, HttpMethod.GET, entity, returnClass);

        if (response == null || response.getStatusCodeValue() != 200) {
            throw new IngestException("members Not Found at " + apiEndpoint);
        }
        return response.getBody();
    }

    private String getFormattedUkEndpoint(final String house) {
        return String.format(API_UK_PARLIAMENT, house);
    }

}
