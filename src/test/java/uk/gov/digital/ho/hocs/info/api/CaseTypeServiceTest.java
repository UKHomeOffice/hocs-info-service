package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseTypeRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.HolidayDateRepository;
import uk.gov.digital.ho.hocs.info.security.UserPermissionsService;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CaseTypeServiceTest {

    @Mock
    private CaseTypeRepository caseTypeRepository;

    @Mock
    private HolidayDateRepository holidayDateRepository;

    @Mock
    private StageTypeService stageTypeService;

    @Mock
    private UserPermissionsService userPermissionsService;

    private CaseTypeService caseTypeService;
    private Set<UUID> team = new HashSet<UUID>() {{  add(UUID.fromString("74c79583-1375-494c-9883-f574e7e36541"));}};
    Set<String> teamString = team.stream().map(uuid -> uuid.toString()).collect(Collectors.toSet());
    private Set<UUID> teams = new HashSet<UUID>() {{ add(UUID.fromString("74c79583-1375-494c-9883-f574e7e36541"));
                                                add(UUID.fromString("8b532de4-4915-4783-a19a-c79fd6754d5c"));}};
    Set<String> teamsString = teams.stream().map(uuid -> uuid.toString()).collect(Collectors.toSet());
    private UUID unitUUID = UUID.randomUUID();

    @Before
    public void setUp() {
        this.caseTypeService = new CaseTypeService(caseTypeRepository,holidayDateRepository, stageTypeService,userPermissionsService);
    }

    @Test
    public void shouldReturnEmptySetWhenNoRolesSet() {
        Set<CaseType> caseTypeDtos = caseTypeService.getAllCaseTypesForUser(false);
        verify(caseTypeRepository, times(0)).findAllCaseTypesByTeam(any());

        assertThat(caseTypeDtos.size()).isEqualTo(0);

    }

    @Test
    public void shouldGetCaseTypesSingleTenantRequested() {

        when(userPermissionsService.getUserTeams()).thenReturn(team);
        when(caseTypeRepository.findAllCaseTypesByTeam(teamString)).thenReturn(getDCUCaseType());

        Set<CaseType> caseTypeDtos = caseTypeService.getAllCaseTypesForUser(false);

        verify(caseTypeRepository, times(1)).findAllCaseTypesByTeam(any());

        assertThat(caseTypeDtos.size()).isEqualTo(3);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN", "DCU Ministerial", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO", "DCU Treat Official", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "DTEN", "DCU Number 10", unitUUID);
    }


    @Test
    public void shouldGetCaseTypesMultipleTeamsRequested() {
        when(userPermissionsService.getUserTeams()).thenReturn(teams);
        when(caseTypeRepository.findAllCaseTypesByTeam(teamsString)).thenReturn(getDCUAndUKVICaseType());

        Set<CaseType> caseTypeDtos = caseTypeService.getAllCaseTypesForUser(false);

        assertThat(caseTypeDtos.size()).isEqualTo(6);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN", "DCU Ministerial", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO", "DCU Treat Official", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "DTEN", "DCU Number 10", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "IMCB", "UKVI B REF", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "IMCM", "UKVI Ministerial REF", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "UTEN", "UKVI Number 10", unitUUID);
    }

    @Test
    public void shouldGetBulkCaseTypesSingleTeamRequested() {

        when(userPermissionsService.getUserTeams()).thenReturn(team);
        when(caseTypeRepository.findAllBulkCaseTypesByTeam(teamString)).thenReturn(getDCUCaseTypeBulk());

        Set<CaseType> caseTypeDtos = caseTypeService.getAllCaseTypesForUser(true);

        verify(caseTypeRepository, times(1)).findAllBulkCaseTypesByTeam(teamString);

        assertThat(caseTypeDtos.size()).isEqualTo(2);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN", "DCU Ministerial", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO", "DCU Treat Official", unitUUID);
        assetCaseTypeDtoDoesNotContainElement(caseTypeDtos, "DTEN", "DCU Number 10", unitUUID);
    }


    @Test
    public void shouldGetBulkCaseTypesMultipleTeamRequested() {
        when(userPermissionsService.getUserTeams()).thenReturn(teams);
        when(caseTypeRepository.findAllBulkCaseTypesByTeam(teamsString)).thenReturn(getDCUAndUKVICaseTypeBulk());

        Set<CaseType> caseTypeDtos = caseTypeService.getAllCaseTypesForUser(true);

        verify(caseTypeRepository, times(1)).findAllBulkCaseTypesByTeam(teamsString);

        assertThat(caseTypeDtos.size()).isEqualTo(5);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN", "DCU Ministerial", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO", "DCU Treat Official", unitUUID);
        assetCaseTypeDtoDoesNotContainElement(caseTypeDtos, "DTEN", "DCU Number 10", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "IMCB", "UKVI B REF", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "IMCM", "UKVI Ministerial REF", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "UTEN", "UKVI Number 10", unitUUID);
    }

    private void assetCaseTypeDtoContainsCorrectElements(Set<CaseType> caseTypeDtos, String CaseType, String DisplayName, UUID tenant) {
        uk.gov.digital.ho.hocs.info.domain.model.CaseType result1 = caseTypeDtos.stream().filter(x -> CaseType.equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDisplayName()).isEqualTo(DisplayName);
        assertThat(result1.getUnitUUID()).isEqualTo(tenant);
    }

    private void assetCaseTypeDtoDoesNotContainElement(Set<CaseType> caseTypeDtos, String CaseType, String DisplayName, UUID tenant) {
        uk.gov.digital.ho.hocs.info.domain.model.CaseType result1 = caseTypeDtos.stream().filter(x -> CaseType.equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNull();
    }

    private Set<CaseType> getDCUCaseType() {
        return new HashSet<>(Arrays.asList(new CaseType(1L, UUID.randomUUID(), "DCU Ministerial","11", "MIN", unitUUID,"DCU_MIN_DISPATCH", true, true),
                new CaseType(2L,UUID.randomUUID(), "DCU Treat Official","12", "TRO", unitUUID,"DCU_TRO_DISPATCH", true, true),
                new CaseType(3L,UUID.randomUUID(), "DCU Number 10","13", "DTEN", unitUUID,"DCU_DTEN_DISPATCH",  true, true)));
    }

    private Set<CaseType> getDCUCaseTypeBulk() {
        return new HashSet<>(Arrays.asList(new CaseType(1L, UUID.randomUUID(), "DCU Ministerial","21", "MIN", unitUUID,"DCU_MIN_DISPATCH",  true, true),
                new CaseType(2L,UUID.randomUUID(), "DCU Treat Official","22", "TRO", unitUUID,"DCU_MIN_DISPATCH",  true, true)));
    }

    private HashSet<CaseType> getDCUAndUKVICaseType() {
        return new HashSet<>(Arrays.asList(
                new CaseType(1L,UUID.randomUUID(), "DCU Ministerial","31", "MIN", unitUUID,"DCU_MIN_DISPATCH",  true, true),
                new CaseType(2L,UUID.randomUUID(), "DCU Treat Official","32", "TRO", unitUUID,"DCU_TRO_DISPATCH",  true, true),
                new CaseType(3L,UUID.randomUUID(), "DCU Number 10","33", "DTEN", unitUUID,"DCU_DTEN_DISPATCH",  true, true),
                new CaseType(1L,UUID.randomUUID(), "UKVI B REF","34", "IMCB", unitUUID,"DCU_IMCB_DISPATCH",  true, true),
                new CaseType(2L,UUID.randomUUID(), "UKVI Ministerial REF","35", "IMCM", unitUUID,"DCU_IMCM_DISPATCH", true, true),
                new CaseType(3L,UUID.randomUUID(), "UKVI Number 10","36",  "UTEN",unitUUID,"DCU_UTEN_DISPATCH",  true, true)));
    }

    private HashSet<CaseType> getDCUAndUKVICaseTypeBulk() {
        return new HashSet<>(Arrays.asList(
                new CaseType(1L,UUID.randomUUID(), "DCU Ministerial","41", "MIN", unitUUID,"DCU_MIN_DISPATCH",  true, true),
                new CaseType(2L,UUID.randomUUID(), "DCU Treat Official","42", "TRO", unitUUID,"DCU_TRO_DISPATCH",  true, true),
                new CaseType(1L,UUID.randomUUID(), "UKVI B REF","43", "IMCB", unitUUID,"DCU_IMCB_DISPATCH",  true, true),
                new CaseType(2L,UUID.randomUUID(), "UKVI Ministerial REF","44", "IMCM", unitUUID,"DCU_IMCM_DISPATCH",  true, true),
                new CaseType(3L,UUID.randomUUID(), "UKVI Number 10","45", "UTEN", unitUUID, "DCU_UTEN_DISPATCH", true, true)));
    }

}