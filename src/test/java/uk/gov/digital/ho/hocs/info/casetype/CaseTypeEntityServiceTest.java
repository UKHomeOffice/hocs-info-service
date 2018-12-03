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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class CaseTypeEntityServiceTest {

    @Mock
    private CaseTypeRepository caseTypeRepository;

    @Mock
    private RequestData requestData;

    private CaseTypeService caseTypeService;

    private String[] singleRole = {"DCU"};
    private String[] multiRoles = {"DCU","UKVI"};


    @Before
    public void setUp() {
        this.caseTypeService = new CaseTypeService(caseTypeRepository, requestData);
    }

    @Test
    public void shouldreturnEmptySetWhenNoRolesSet() {
        Set<CaseTypeEntity> caseTypeDtos = caseTypeService.getCaseTypes();
        verify(caseTypeRepository, times(1)).findAllCaseTypesByTenant(any());

        assertThat(caseTypeDtos.size()).isEqualTo(0);

    }

    @Test
    public void shouldGetCaseTypesSingleTenantRequested() {

        when(requestData.roles()).thenReturn(singleRole);
        when(caseTypeRepository.findAllCaseTypesByTenant(any())).thenReturn(getDCUCaseType());

        Set<CaseTypeEntity> caseTypeDtos = caseTypeService.getCaseTypes();

        verify(caseTypeRepository, times(1)).findAllCaseTypesByTenant(any());

        assertThat(caseTypeDtos.size()).isEqualTo(3);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN", "DCU Ministerial", "DCU");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO", "DCU Treat Official", "DCU");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "DTEN", "DCU Number 10", "DCU");
    }


    @Test
    public void shouldGetCaseTypesMultipleTenantRequested() {
        when(requestData.roles()).thenReturn(multiRoles);
        when(caseTypeRepository.findAllCaseTypesByTenant(multiRoles)).thenReturn(getDCUAndUKVICaseType());

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
    public void shouldGetBulkCaseTypesSingleTenantRequested() {

        when(requestData.roles()).thenReturn(singleRole);
        when(caseTypeRepository.findAllBulkCaseTypesByTenant(any())).thenReturn(getDCUCaseTypeBulk());

        Set<CaseTypeEntity> caseTypeDtos = caseTypeService.getCaseTypesBulk();

        verify(caseTypeRepository, times(1)).findAllBulkCaseTypesByTenant(any());

        assertThat(caseTypeDtos.size()).isEqualTo(2);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN", "DCU Ministerial", "DCU");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO", "DCU Treat Official", "DCU");
        assetCaseTypeDtoDoesNotContainElement(caseTypeDtos, "DTEN", "DCU Number 10", "DCU");
    }


    @Test
    public void shouldGetBulkCaseTypesMultipleTenantRequested() {
        when(requestData.roles()).thenReturn(multiRoles);
        when(caseTypeRepository.findAllBulkCaseTypesByTenant(multiRoles)).thenReturn(getDCUAndUKVICaseTypeBulk());

        Set<CaseTypeEntity> caseTypeDtos = caseTypeService.getCaseTypesBulk();

        verify(caseTypeRepository, times(1)).findAllBulkCaseTypesByTenant(any());

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
        return new HashSet<>(Arrays.asList(new CaseTypeEntity(1L, "DCU Ministerial","11", "MIN", "DCU", true),
                new CaseTypeEntity(2L, "DCU Treat Official","12", "TRO", "DCU", true),
                new CaseTypeEntity(3L, "DCU Number 10","13", "DTEN", "DCU", true)));
    }

    private Set<CaseTypeEntity> getDCUCaseTypeBulk() {
        return new HashSet<>(Arrays.asList(new CaseTypeEntity(1L, "DCU Ministerial","21", "MIN", "DCU", true),
                new CaseTypeEntity(2L, "DCU Treat Official","22", "TRO", "DCU", true)));
    }

    private HashSet<CaseTypeEntity> getDCUAndUKVICaseType() {
        return new HashSet<>(Arrays.asList(
                new CaseTypeEntity(1L, "DCU Ministerial","31", "MIN", "DCU", true),
                new CaseTypeEntity(2L, "DCU Treat Official","32", "TRO", "DCU", true),
                new CaseTypeEntity(3L, "DCU Number 10","33", "DTEN", "DCU", true),
                new CaseTypeEntity(1L, "UKVI B REF","34", "IMCB", "UKVI", true),
                new CaseTypeEntity(2L, "UKVI Ministerial REF","35", "IMCM", "UKVI", true),
                new CaseTypeEntity(3L, "UKVI Number 10","36",  "UTEN","UKVI", true)));
    }

    private HashSet<CaseTypeEntity> getDCUAndUKVICaseTypeBulk() {
        return new HashSet<>(Arrays.asList(
                new CaseTypeEntity(1L, "DCU Ministerial","41", "MIN", "DCU", true),
                new CaseTypeEntity(2L, "DCU Treat Official","42", "TRO", "DCU", true),
                new CaseTypeEntity(1L, "UKVI B REF","43", "IMCB", "UKVI", true),
                new CaseTypeEntity(2L, "UKVI Ministerial REF","44", "IMCM", "UKVI", true),
                new CaseTypeEntity(3L, "UKVI Number 10","45", "UTEN", "UKVI", true)));
    }

}