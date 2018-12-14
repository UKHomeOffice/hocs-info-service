package uk.gov.digital.ho.hocs.info.casetype;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.dto.CaseTypeDto;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.CaseTypeRepository;
import uk.gov.digital.ho.hocs.info.security.UserPermissionsService;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class CaseTypeEntityServiceTest {

    @Mock
    private CaseTypeRepository caseTypeRepository;

    @Mock
    private RequestData requestData;

    @Mock
    private UserPermissionsService userPermissionsService;

    private CaseTypeService caseTypeService;
    private Set team = new HashSet<String>() {{ add("74c79583-1375-494c-9883-f574e7e36541");}};
    private Set teams = new HashSet<String>() {{ add("74c79583-1375-494c-9883-f574e7e36541, 8b532de4-4915-4783-a19a-c79fd6754d5c"); }};
    @Before
    public void setUp() {
        this.caseTypeService = new CaseTypeService(caseTypeRepository,userPermissionsService);
    }

    @Test
    public void shouldreturnEmptySetWhenNoRolesSet() {
        Set<CaseTypeEntity> caseTypeDtos = caseTypeService.getCaseTypes();
        verify(caseTypeRepository, times(1)).findAllCaseTypesByTeam(any());

        assertThat(caseTypeDtos.size()).isEqualTo(0);

    }

    @Test
    public void shouldGetCaseTypesSingleTenantRequested() {

        when(userPermissionsService.getUserTeams()).thenReturn(team);
        when(caseTypeRepository.findAllCaseTypesByTeam(team)).thenReturn(getDCUCaseType());

        Set<CaseTypeEntity> caseTypeDtos = caseTypeService.getCaseTypes();

        verify(caseTypeRepository, times(1)).findAllCaseTypesByTeam(any());

        assertThat(caseTypeDtos.size()).isEqualTo(3);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN", "DCU Ministerial", "DCU");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO", "DCU Treat Official", "DCU");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "DTEN", "DCU Number 10", "DCU");
    }


    @Test
    public void shouldGetCaseTypesMultipleTeamsRequested() {
        when(userPermissionsService.getUserTeams()).thenReturn(teams);
        when(caseTypeRepository.findAllCaseTypesByTeam(teams)).thenReturn(getDCUAndUKVICaseType());

        Set<CaseTypeEntity> caseTypeDtos = caseTypeService.getCaseTypes();

        assertThat(caseTypeDtos.size()).isEqualTo(6);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN", "DCU Ministerial", "DCU");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO", "DCU Treat Official", "DCU");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "DTEN", "DCU Number 10", "DCU");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "IMCB", "UKVI B REF", "UKVI");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "IMCM", "UKVI Ministerial REF", "UKVI");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "UTEN", "UKVI Number 10", "UKVI");
    }

    @Test
    public void shouldGetBulkCaseTypesSingleTeamRequested() {

        when(userPermissionsService.getUserTeams()).thenReturn(team);
        when(caseTypeRepository.findAllBulkCaseTypesByTeam(team)).thenReturn(getDCUCaseTypeBulk());

        Set<CaseTypeEntity> caseTypeDtos = caseTypeService.getCaseTypesBulk();

        verify(caseTypeRepository, times(1)).findAllBulkCaseTypesByTeam(team);

        assertThat(caseTypeDtos.size()).isEqualTo(2);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN", "DCU Ministerial", "DCU");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO", "DCU Treat Official", "DCU");
        assetCaseTypeDtoDoesNotContainElement(caseTypeDtos, "DTEN", "DCU Number 10", "DCU");
    }


    @Test
    public void shouldGetBulkCaseTypesMultipleTeamRequested() {
        when(userPermissionsService.getUserTeams()).thenReturn(teams);
        when(caseTypeRepository.findAllBulkCaseTypesByTeam(teams)).thenReturn(getDCUAndUKVICaseTypeBulk());

        Set<CaseTypeEntity> caseTypeDtos = caseTypeService.getCaseTypesBulk();

        verify(caseTypeRepository, times(1)).findAllBulkCaseTypesByTeam(teams);

        assertThat(caseTypeDtos.size()).isEqualTo(5);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN", "DCU Ministerial", "DCU");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO", "DCU Treat Official", "DCU");
        assetCaseTypeDtoDoesNotContainElement(caseTypeDtos, "DTEN", "DCU Number 10", "DCU");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "IMCB", "UKVI B REF", "UKVI");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "IMCM", "UKVI Ministerial REF", "UKVI");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "UTEN", "UKVI Number 10", "UKVI");
    }

    private void assetCaseTypeDtoContainsCorrectElements(Set<CaseTypeEntity> caseTypeDtos, String CaseType, String DisplayName, String tenant) {
        CaseTypeEntity result1 = caseTypeDtos.stream().filter(x -> CaseType.equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDisplayName()).isEqualTo(DisplayName);
        assertThat(result1.getRole()).isEqualTo(tenant);
    }

    private void assetCaseTypeDtoDoesNotContainElement(Set<CaseTypeEntity> caseTypeDtos, String CaseType, String DisplayName, String tenant) {
        CaseTypeEntity result1 = caseTypeDtos.stream().filter(x -> CaseType.equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNull();
    }

    private Set<CaseTypeEntity> getDCUCaseType() {
        return new HashSet<>(Arrays.asList(new CaseTypeEntity(1L, "DCU Ministerial","11", "MIN", "DCU","DCU_MIN_DISPATCH", true),
                new CaseTypeEntity(2L, "DCU Treat Official","12", "TRO", "DCU","DCU_TRO_DISPATCH",  true),
                new CaseTypeEntity(3L, "DCU Number 10","13", "DTEN", "DCU","DCU_DTEN_DISPATCH",  true)));
    }

    private Set<CaseTypeEntity> getDCUCaseTypeBulk() {
        return new HashSet<>(Arrays.asList(new CaseTypeEntity(1L, "DCU Ministerial","21", "MIN", "DCU","DCU_MIN_DISPATCH",  true),
                new CaseTypeEntity(2L, "DCU Treat Official","22", "TRO", "DCU","DCU_MIN_DISPATCH",  true)));
    }

    private HashSet<CaseTypeEntity> getDCUAndUKVICaseType() {
        return new HashSet<>(Arrays.asList(
                new CaseTypeEntity(1L, "DCU Ministerial","31", "MIN", "DCU","DCU_MIN_DISPATCH",  true),
                new CaseTypeEntity(2L, "DCU Treat Official","32", "TRO", "DCU","DCU_TRO_DISPATCH",  true),
                new CaseTypeEntity(3L, "DCU Number 10","33", "DTEN", "DCU","DCU_DTEN_DISPATCH",  true),
                new CaseTypeEntity(1L, "UKVI B REF","34", "IMCB", "UKVI","DCU_IMCB_DISPATCH",  true),
                new CaseTypeEntity(2L, "UKVI Ministerial REF","35", "IMCM",  "UKVI","DCU_IMCM_DISPATCH", true),
                new CaseTypeEntity(3L, "UKVI Number 10","36",  "UTEN","UKVI","DCU_UTEN_DISPATCH",  true)));
    }

    private HashSet<CaseTypeEntity> getDCUAndUKVICaseTypeBulk() {
        return new HashSet<>(Arrays.asList(
                new CaseTypeEntity(1L, "DCU Ministerial","41", "MIN", "DCU","DCU_MIN_DISPATCH",  true),
                new CaseTypeEntity(2L, "DCU Treat Official","42", "TRO", "DCU","DCU_TRO_DISPATCH",  true),
                new CaseTypeEntity(1L, "UKVI B REF","43", "IMCB", "UKVI","DCU_IMCB_DISPATCH",  true),
                new CaseTypeEntity(2L, "UKVI Ministerial REF","44", "IMCM", "UKVI","DCU_IMCM_DISPATCH",  true),
                new CaseTypeEntity(3L, "UKVI Number 10","45", "UTEN", "UKVI", "DCU_UTEN_DISPATCH", true)));
    }

}