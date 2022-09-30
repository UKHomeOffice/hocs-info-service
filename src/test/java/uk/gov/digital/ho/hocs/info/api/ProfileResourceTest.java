package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.ProfileDto;
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;
import uk.gov.digital.ho.hocs.info.domain.model.Profile;
import uk.gov.digital.ho.hocs.info.domain.repository.ProfileRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProfileResourceTest {

    private ProfileResource profileResource;

    @Mock
    CaseTypeService caseTypeService;

    @Mock
    ProfileRepository profileRepository;

    @Before
    public void before() {
        profileResource = new ProfileResource(caseTypeService, profileRepository);
    }

    @Test
    public void getProfileNameForUser_noCaseTypes() {
        when(caseTypeService.getAllCaseTypesForUser(false, true)).thenReturn(Set.of());

        ResponseEntity<List<String>> response = profileResource.getProfileNameForUser(true);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(List.of());

        verify(caseTypeService).getAllCaseTypesForUser(false, true);
        verifyNoMoreInteractions(caseTypeService, profileRepository);

    }

    @Test
    public void getProfileNameForUser() {
        CaseType caseType1 = new CaseType(null, UUID.randomUUID(), "TEST1", "a1", "type1", UUID.randomUUID(), "TEST",
            true, true, null, null);
        CaseType caseType2 = new CaseType(null, UUID.randomUUID(), "TEST2", "a2", "type2", UUID.randomUUID(), "TEST",
            true, true, null, null);

        List<String> expectedResult = new ArrayList<>(Arrays.asList("Profile1", "Profile2"));
        when(caseTypeService.getAllCaseTypesForUser(false, true)).thenReturn(Set.of(caseType1, caseType2));
        when(profileRepository.findAllProfileNamesByCaseTypesAndSystemName(anyList(), eq("system"))).thenReturn(
            expectedResult);

        ResponseEntity<List<String>> response = profileResource.getProfileNameForUser(true);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expectedResult);

        verify(caseTypeService).getAllCaseTypesForUser(false, true);
        verify(profileRepository).findAllProfileNamesByCaseTypesAndSystemName(anyList(), eq("system"));
        verifyNoMoreInteractions(caseTypeService, profileRepository);

    }

    @Test
    public void getProfileForCaseType() {
        String caseType = "C1";

        Profile profile = new Profile("testProfile", "system", true);

        when(profileRepository.findByCaseTypeAndSystemName(caseType, "system")).thenReturn(profile);

        ResponseEntity<ProfileDto> response = profileResource.getProfileForCaseType(caseType);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getProfileName()).isEqualTo("testProfile");
        assertThat(response.getBody().isSummaryDeadlineEnabled()).isTrue();

        verify(profileRepository).findByCaseTypeAndSystemName(caseType, "system");
        verifyNoMoreInteractions(caseTypeService, profileRepository);

    }

    @Test
    public void getProfileForCaseType_nullProfile() {
        String caseType = "C1";

        when(profileRepository.findByCaseTypeAndSystemName(caseType, "system")).thenReturn(null);

        ResponseEntity<ProfileDto> response = profileResource.getProfileForCaseType(caseType);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNull();

        verify(profileRepository).findByCaseTypeAndSystemName(caseType, "system");
        verifyNoMoreInteractions(caseTypeService, profileRepository);

    }

}
