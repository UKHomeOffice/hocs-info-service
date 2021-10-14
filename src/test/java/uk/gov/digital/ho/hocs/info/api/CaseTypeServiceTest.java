package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.CaseTypeActionDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateCaseTypeDto;
import uk.gov.digital.ho.hocs.info.domain.model.*;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseActionTypeRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseTypeRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.DocumentTagRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.HolidayDateRepository;
import uk.gov.digital.ho.hocs.info.security.UserPermissionsService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CaseTypeServiceTest {

    @Mock
    private CaseTypeRepository caseTypeRepository;

    @Mock
    private DocumentTagRepository documentTagRepository;

    @Mock
    private HolidayDateRepository holidayDateRepository;

    @Mock
    private StageTypeService stageTypeService;

    @Mock
    private UserPermissionsService userPermissionsService;

    @Mock
    private LocalDateWrapper localDateWrapper;

    @Mock
    private CaseActionTypeRepository caseActionTypeRepository;

    private CaseTypeService caseTypeService;
    private Set<UUID> team = new HashSet<UUID>() {{  add(UUID.fromString("74c79583-1375-494c-9883-f574e7e36541"));}};
    Set<String> teamString = team.stream().map(uuid -> uuid.toString()).collect(Collectors.toSet());
    private Set<UUID> teams = new HashSet<UUID>() {{ add(UUID.fromString("74c79583-1375-494c-9883-f574e7e36541"));
                                                add(UUID.fromString("8b532de4-4915-4783-a19a-c79fd6754d5c"));}};
    Set<String> teamsString = teams.stream().map(uuid -> uuid.toString()).collect(Collectors.toSet());
    private UUID unitUUID = UUID.randomUUID();

    private static final String CASE_TYPE = "CaseType1";

    @Before
    public void setUp() {
        this.caseTypeService = new CaseTypeService(caseTypeRepository,documentTagRepository,holidayDateRepository, caseActionTypeRepository, stageTypeService,userPermissionsService, localDateWrapper);
    }

    @Test
    public void shouldReturnEmptySetWhenNoRolesSet() {
        Set<CaseType> caseTypeDtos = caseTypeService.getAllCaseTypesForUser(false, false);
        verify(caseTypeRepository, times(0)).findAllCaseTypesByTeam(any(), eq(false));

        assertThat(caseTypeDtos.size()).isEqualTo(0);

    }

    @Test
    public void shouldGetCaseTypesSingleTenantRequested() {

        when(userPermissionsService.getUserTeams()).thenReturn(team);
        when(caseTypeRepository.findAllCaseTypesByTeam(teamString, false)).thenReturn(getDCUCaseType());

        Set<CaseType> caseTypeDtos = caseTypeService.getAllCaseTypesForUser(false, false);

        verify(caseTypeRepository, times(1)).findAllCaseTypesByTeam(any(), eq(false));

        assertThat(caseTypeDtos.size()).isEqualTo(3);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN", "DCU Ministerial", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO", "DCU Treat Official", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "DTEN", "DCU Number 10", unitUUID);
    }


    @Test
    public void shouldGetCaseTypesMultipleTeamsRequested() {
        when(userPermissionsService.getUserTeams()).thenReturn(teams);
        when(caseTypeRepository.findAllCaseTypesByTeam(teamsString, false)).thenReturn(getDCUAndUKVICaseType());

        Set<CaseType> caseTypeDtos = caseTypeService.getAllCaseTypesForUser(false, false);

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
        when(caseTypeRepository.findAllBulkCaseTypesByTeam(teamString, false)).thenReturn(getDCUCaseTypeBulk());

        Set<CaseType> caseTypeDtos = caseTypeService.getAllCaseTypesForUser(true, false);

        verify(caseTypeRepository, times(1)).findAllBulkCaseTypesByTeam(teamString, false);

        assertThat(caseTypeDtos.size()).isEqualTo(2);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN", "DCU Ministerial", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO", "DCU Treat Official", unitUUID);
        assetCaseTypeDtoDoesNotContainElement(caseTypeDtos, "DTEN", "DCU Number 10", unitUUID);
    }


    @Test
    public void shouldGetBulkCaseTypesMultipleTeamRequested() {
        when(userPermissionsService.getUserTeams()).thenReturn(teams);
        when(caseTypeRepository.findAllBulkCaseTypesByTeam(teamsString, false)).thenReturn(getDCUAndUKVICaseTypeBulk());

        Set<CaseType> caseTypeDtos = caseTypeService.getAllCaseTypesForUser(true, false);

        verify(caseTypeRepository, times(1)).findAllBulkCaseTypesByTeam(teamsString, false);

        assertThat(caseTypeDtos.size()).isEqualTo(5);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN", "DCU Ministerial", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO", "DCU Treat Official", unitUUID);
        assetCaseTypeDtoDoesNotContainElement(caseTypeDtos, "DTEN", "DCU Number 10", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "IMCB", "UKVI B REF", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "IMCM", "UKVI Ministerial REF", unitUUID);
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "UTEN", "UKVI Number 10", unitUUID);
    }

    @Test
    public void shouldCreateNewCaseTypeInRepository() {

        CreateCaseTypeDto caseType = new CreateCaseTypeDto("New Case Type", "c1", "NEW",true,true,"STAGE_ONE", "PREV");

        CaseType response = mock(CaseType.class);
        when(caseTypeRepository.save(any(CaseType.class))).thenReturn(response);

        caseTypeService.createCaseType(caseType);

        verify(caseTypeRepository, times(1)).save(any(CaseType.class));
    }

    @Test
    public void shouldGetDeadlineWarningForCaseType(){
        LocalDate receivedDate = LocalDate.of(2020,5,8); // Friday
        CaseType caseType = new CaseType();
        when(caseTypeRepository.findByType(CASE_TYPE)).thenReturn(caseType);
        Set<ExemptionDate> exemptions = Set.of(new ExemptionDate(1L, LocalDate.parse("2020-05-11")), new ExemptionDate(1L, LocalDate.parse("2020-05-12")));
        when(holidayDateRepository.findAllByCaseType(caseType.getUuid())).thenReturn(exemptions);

        LocalDate response = caseTypeService.getDeadlineWarningForCaseType(CASE_TYPE,receivedDate,1);

        assertThat(response).isEqualTo(LocalDate.of(2020,5,13));
        verify(caseTypeRepository).findByType(CASE_TYPE);
        verifyNoMoreInteractions(caseTypeRepository);
    }

    @Test
    public void shoudGetAllStageDeadlinesForCaseType(){
        CaseType caseType = new CaseType();
        when(caseTypeRepository.findByType(CASE_TYPE)).thenReturn(caseType);
        Set<StageTypeEntity> stageTypes = new HashSet<>();
        stageTypes.add(new StageTypeEntity(1L, UUID.randomUUID(), "stage 8", "111","STAGE_TYPE_8", UUID.randomUUID(),8,8,8,true,null));
        stageTypes.add(new StageTypeEntity(1L, UUID.randomUUID(), "stage 2", "111","STAGE_TYPE_2", UUID.randomUUID(),2,2,2,true,null));
        when(stageTypeService.getAllStageTypesByCaseType(caseType.getUuid())).thenReturn(stageTypes);

        Map<String, LocalDate> response = caseTypeService.getAllStageDeadlinesForCaseType(CASE_TYPE, LocalDate.now());

        assertThat(response.size()).isEqualTo(2);
        Iterator<Map.Entry<String, LocalDate>> iterator = response.entrySet().iterator();
        Map.Entry<String, LocalDate> first = iterator.next();
        Map.Entry<String, LocalDate> second = iterator.next();
        assertThat(first.getValue()).isBefore(second.getValue());
    }

    @Test
    public void shouldGetCaseDeadline(){
        CaseType caseType = new CaseType();

        String receivedDateString = "2020-08-03";
        String expectedExtendedDateString = "2020-09-07";

        LocalDate receivedDate = LocalDate.parse(receivedDateString);
        LocalDate expectedExtendedDate = LocalDate.parse(expectedExtendedDateString);


        when(caseTypeRepository.findByType(CASE_TYPE)).thenReturn(caseType);

        LocalDate response = caseTypeService.getDeadlineForCaseType(CASE_TYPE, receivedDate, 10, 15);

        assertThat(response).isEqualTo(expectedExtendedDate);
    }

    @Test
    public void shouldGetDocumentTagsForCaseType(){
        List<DocumentTag> documentTags = new ArrayList<>();
        documentTags.add((new DocumentTag(null,null,null,"First",(short)1)));
        documentTags.add((new DocumentTag(null,null,null,"Second",(short)2)));
        when(documentTagRepository.findByCaseType("TEST")).thenReturn(documentTags);

        List<String> tags = caseTypeService.getDocumentTagsForCaseType("TEST");

        assertThat(tags.size()).isEqualTo(2);
        assertThat(tags.get(0)).isEqualTo("First");
        assertThat(tags.get(1)).isEqualTo("Second");
        verify(documentTagRepository).findByCaseType("TEST");
        verifyNoMoreInteractions(documentTagRepository);
    }

    @Test
    public void shouldCalculateWorkingDaysElapsedForCaseType(){
        when(localDateWrapper.now()).thenReturn(LocalDate.parse("2020-05-18"));

        int result = caseTypeService.calculateWorkingDaysElapsedForCaseType(CASE_TYPE, LocalDate.parse("2020-05-11"));

        assertThat(result).isEqualTo(5);
        verify(localDateWrapper).now();
        verify(holidayDateRepository).findAllByCaseType(CASE_TYPE);
        checkNoMoreInteractions();
    }

    @Test
    public void shouldCalculateWorkingDaysElapsedForCaseType_sameDay(){
        when(localDateWrapper.now()).thenReturn(LocalDate.parse("2020-05-18"));

        int result = caseTypeService.calculateWorkingDaysElapsedForCaseType(CASE_TYPE, LocalDate.parse("2020-05-18"));

        assertThat(result).isEqualTo(0);
        verify(localDateWrapper).now();
        checkNoMoreInteractions();
    }

    @Test
    public void shouldCalculateWorkingDaysElapsedForCaseType_weekend(){
        when(localDateWrapper.now()).thenReturn(LocalDate.parse("2020-05-17"));

        int result = caseTypeService.calculateWorkingDaysElapsedForCaseType(CASE_TYPE, LocalDate.parse("2020-05-16"));

        assertThat(result).isEqualTo(0);
        verify(localDateWrapper).now();
        verify(holidayDateRepository).findAllByCaseType(CASE_TYPE);
        checkNoMoreInteractions();
    }

    @Test
    public void shouldCalculateWorkingDaysElapsedForCaseType_withExemptions(){
        when(localDateWrapper.now()).thenReturn(LocalDate.parse("2020-05-18"));
        List<ExemptionDate> exemptions = List.of(new ExemptionDate(1L, LocalDate.parse("2020-05-11")), new ExemptionDate(1L, LocalDate.parse("2020-05-12")));
        when(holidayDateRepository.findAllByCaseType(CASE_TYPE)).thenReturn(exemptions);

        int result = caseTypeService.calculateWorkingDaysElapsedForCaseType(CASE_TYPE, LocalDate.parse("2020-05-11"));

        assertThat(result).isEqualTo(3);
        verify(localDateWrapper).now();
        verify(holidayDateRepository).findAllByCaseType(CASE_TYPE);
        checkNoMoreInteractions();
    }

    @Test
    public void shouldCalculateWorkingDaysElapsedForCaseType_nullDate(){
        when(localDateWrapper.now()).thenReturn(LocalDate.parse("2020-05-18"));
        int result = caseTypeService.calculateWorkingDaysElapsedForCaseType(CASE_TYPE, null);

        assertThat(result).isEqualTo(0);
        verify(localDateWrapper).now();
        checkNoMoreInteractions();
    }

    @Test
    public void shouldCalculateWorkingDaysElapsedForCaseType_futureDate(){
        when(localDateWrapper.now()).thenReturn(LocalDate.parse("2020-05-18"));
        int result = caseTypeService.calculateWorkingDaysElapsedForCaseType(CASE_TYPE, LocalDate.parse("2020-05-19"));

        assertThat(result).isEqualTo(0);
        verify(localDateWrapper).now();
        checkNoMoreInteractions();
    }

    private void checkNoMoreInteractions(){
        verifyNoMoreInteractions(caseTypeRepository, documentTagRepository, holidayDateRepository, stageTypeService, userPermissionsService, localDateWrapper);
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
        return new HashSet<>(Arrays.asList(new CaseType(1L, UUID.randomUUID(), "DCU Ministerial","11", "MIN", unitUUID,"DCU_MIN_DISPATCH", true, true, null),
                new CaseType(2L,UUID.randomUUID(), "DCU Treat Official","12", "TRO", unitUUID,"DCU_TRO_DISPATCH", true, true, null),
                new CaseType(3L,UUID.randomUUID(), "DCU Number 10","13", "DTEN", unitUUID,"DCU_DTEN_DISPATCH",  true, true, null)));
    }

    private Set<CaseType> getDCUCaseTypeBulk() {
        return new HashSet<>(Arrays.asList(new CaseType(1L, UUID.randomUUID(), "DCU Ministerial","21", "MIN", unitUUID,"DCU_MIN_DISPATCH",  true, true, null),
                new CaseType(2L,UUID.randomUUID(), "DCU Treat Official","22", "TRO", unitUUID,"DCU_MIN_DISPATCH",  true, true, null)));
    }

    private HashSet<CaseType> getDCUAndUKVICaseType() {
        return new HashSet<>(Arrays.asList(
                new CaseType(1L,UUID.randomUUID(), "DCU Ministerial","31", "MIN", unitUUID,"DCU_MIN_DISPATCH",  true, true, null),
                new CaseType(2L,UUID.randomUUID(), "DCU Treat Official","32", "TRO", unitUUID,"DCU_TRO_DISPATCH",  true, true, null),
                new CaseType(3L,UUID.randomUUID(), "DCU Number 10","33", "DTEN", unitUUID,"DCU_DTEN_DISPATCH",  true, true, null),
                new CaseType(1L,UUID.randomUUID(), "UKVI B REF","34", "IMCB", unitUUID,"DCU_IMCB_DISPATCH",  true, true, null),
                new CaseType(2L,UUID.randomUUID(), "UKVI Ministerial REF","35", "IMCM", unitUUID,"DCU_IMCM_DISPATCH", true, true, null),
                new CaseType(3L,UUID.randomUUID(), "UKVI Number 10","36",  "UTEN",unitUUID,"DCU_UTEN_DISPATCH",  true, true, null)));
    }

    private HashSet<CaseType> getDCUAndUKVICaseTypeBulk() {
        return new HashSet<>(Arrays.asList(
                new CaseType(1L,UUID.randomUUID(), "DCU Ministerial","41", "MIN", unitUUID,"DCU_MIN_DISPATCH",  true, true, null),
                new CaseType(2L,UUID.randomUUID(), "DCU Treat Official","42", "TRO", unitUUID,"DCU_TRO_DISPATCH",  true, true, null),
                new CaseType(1L,UUID.randomUUID(), "UKVI B REF","43", "IMCB", unitUUID,"DCU_IMCB_DISPATCH",  true, true, null),
                new CaseType(2L,UUID.randomUUID(), "UKVI Ministerial REF","44", "IMCM", unitUUID,"DCU_IMCM_DISPATCH",  true, true, null),
                new CaseType(3L,UUID.randomUUID(), "UKVI Number 10","45", "UTEN", unitUUID, "DCU_UTEN_DISPATCH", true, true, null)));
    }

    @Test
    public void getCaseActionsByType_returnsActions() {

        // GIVEN
        UUID rand1 = UUID.randomUUID();
        UUID rand2 = UUID.randomUUID();
        UUID rand3 = UUID.randomUUID();
        UUID rand4 = UUID.randomUUID();

        CaseTypeAction mockCaseActionType1 = new CaseTypeAction(
                rand1,
                rand2,
                "CaseType1",
                "ACTION_2",
                "ACTION_LABEL",
                true,
                "{}",
                10,
                LocalDateTime.MIN,
                LocalDateTime.MIN
        );

        CaseTypeAction mockCaseActionType2 = new CaseTypeAction(
                rand3,
                rand4,
                "CaseType1",
                "ACTION_2",
                "ACTION_LABEL",
                true,
                "{}",
                20,
                LocalDateTime.MIN,
                LocalDateTime.MIN
        );

        CaseTypeActionDto expectedCaseTypeDto1 = CaseTypeActionDto.from(mockCaseActionType1);
        CaseTypeActionDto expectedCaseTypeDto2 = CaseTypeActionDto.from(mockCaseActionType2);

        when(caseActionTypeRepository.findAllByCaseTypeAndActiveIsTrue(CASE_TYPE)).thenReturn(List.of(mockCaseActionType1, mockCaseActionType2));

        // WHEN
        List<CaseTypeActionDto> output = caseTypeService.getCaseActionsByCaseType(CASE_TYPE);

        // THEN
        assertEquals(expectedCaseTypeDto1.getActionType(), output.get(0).getActionType());
        assertEquals(expectedCaseTypeDto2.getActionType(), output.get(1).getActionType());
        verify(caseActionTypeRepository, times(1)).findAllByCaseTypeAndActiveIsTrue(any());
    }

}
