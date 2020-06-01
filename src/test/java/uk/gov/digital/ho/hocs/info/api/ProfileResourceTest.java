package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;
import uk.gov.digital.ho.hocs.info.domain.repository.ProfileRepository;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

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
        when(caseTypeService.getAllCaseTypesForUser(false)).thenReturn(Set.of());

        ResponseEntity<List<String>> response = profileResource.getProfileNameForUser();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(List.of());

        verify(caseTypeService).getAllCaseTypesForUser(false);
        verifyNoMoreInteractions(caseTypeService, profileRepository);


    }

    @Test
    public void getProfileNameForUser() {
        CaseType caseType1 = new CaseType(null, UUID.randomUUID(),"TEST1","a1","type1",UUID.randomUUID(),"TEST", true, true);
        CaseType caseType2 = new CaseType(null,UUID.randomUUID(),"TEST2","a2","type2",UUID.randomUUID(),"TEST", true, true);

        List<String> expectedResult = new ArrayList<>(Arrays.asList("Profile1", "Profile2"));
        when(caseTypeService.getAllCaseTypesForUser(false)).thenReturn(Set.of(caseType1, caseType2));
        when(profileRepository.findAllProfileNamesByCaseTypesAndSystemName(anyList(), eq("system"))).thenReturn(expectedResult);

        ResponseEntity<List<String>> response = profileResource.getProfileNameForUser();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expectedResult);

        verify(caseTypeService).getAllCaseTypesForUser(false);
        verify(profileRepository).findAllProfileNamesByCaseTypesAndSystemName(anyList(), eq("system"));
        verifyNoMoreInteractions(caseTypeService, profileRepository);


    }
}
