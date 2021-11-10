package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.api.dto.ProfileDto;
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;
import uk.gov.digital.ho.hocs.info.domain.model.Profile;
import uk.gov.digital.ho.hocs.info.domain.repository.ProfileRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class ProfileResource {

    private final CaseTypeService caseTypeService;
    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileResource(CaseTypeService caseTypeService, ProfileRepository profileRepository) {
        this.caseTypeService = caseTypeService;
        this.profileRepository = profileRepository;
    }

    @GetMapping(value = "/profileNames", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<String>> getProfileNameForUser(@RequestParam(required = false, name = "initialCaseType", defaultValue = "true") Boolean initialCaseType ) {
        List<String> userCaseTypes = caseTypeService.getAllCaseTypesForUser(false, initialCaseType).stream().map(CaseType::getType).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(userCaseTypes)) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(profileRepository.findAllProfileNamesByCaseTypesAndSystemName(userCaseTypes, "system"));

    }

    @Cacheable("getProfileForCaseType")
    @GetMapping(value = "/profile/forcasetype/{caseType}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ProfileDto> getProfileForCaseType(@PathVariable String caseType) {
        Profile profile = profileRepository.findByCaseTypeAndSystemName(caseType, "system");

        if (profile != null) {
            return ResponseEntity.ok(ProfileDto.from(profile));
        }
        return ResponseEntity.ok(null);

    }

}
