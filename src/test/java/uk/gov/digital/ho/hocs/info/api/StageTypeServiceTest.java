package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.domain.model.ExemptionDate;
import uk.gov.digital.ho.hocs.info.domain.model.StageTypeEntity;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.repository.HolidayDateRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.StageTypeRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StageTypeServiceTest {

    @Mock
    private StageTypeRepository stageTypeRepository;

    @Mock
    private HolidayDateRepository holidayDateRepository;

    private StageTypeService service;
    private final Team team = new Team( "Team1" , new HashSet<>());

    @Before
    public void setup() {
        service = new StageTypeService(stageTypeRepository, holidayDateRepository);
    }

    @Test
    public void shouldGetAllStageTypes() {
        StageTypeEntity stage = new StageTypeEntity(1L, UUID.randomUUID(), "stage name", "111","STAGE_TYPE", UUID.randomUUID(),1,1,true, team);
        Set<StageTypeEntity> stages = new HashSet<StageTypeEntity>() {{
            add(stage);
        }};
        when(stageTypeRepository.findAllBy()).thenReturn(stages);

        Set<StageTypeEntity> result = service.getAllStageTypes();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.toArray()[0]).isEqualTo(stage);
    }

    @Test
    public void shouldGetTeamForStageType() {
        StageTypeEntity stage = new StageTypeEntity(1L, UUID.randomUUID(), "stage name", "111","STAGE_TYPE", UUID.randomUUID(),1,1,true,team);

        when(stageTypeRepository.findByType("STAGE_TYPE")).thenReturn(stage);

        Team result = service.getTeamForStageType("STAGE_TYPE");
        assertThat(result.getUuid()).isEqualTo(team.getUuid());
        assertThat(result.getDisplayName()).isEqualTo("Team1");
    }

    @Test
    public void shouldGetDeadlineWarningForStageType() {
        StageTypeEntity stage = new StageTypeEntity(1L, UUID.randomUUID(), "stage name", "111","STAGE_TYPE", UUID.randomUUID(),9,1,true,team);
        when(stageTypeRepository.findByType("STAGE_TYPE")).thenReturn(stage);
        Set<ExemptionDate> exemptions = Set.of(new ExemptionDate(1L, LocalDate.parse("2020-05-11")), new ExemptionDate(1L, LocalDate.parse("2020-05-12")));
        when(holidayDateRepository.findAllByStageType("STAGE_TYPE")).thenReturn(exemptions);

        LocalDate result = service.getDeadlineWarningForStageType("STAGE_TYPE", LocalDate.of(2020,5,8),LocalDate.of(2020,5,9));

        assertThat(result).isEqualTo(LocalDate.of(2020,5,13));
        verify(stageTypeRepository).findByType("STAGE_TYPE");
        verifyNoMoreInteractions(stageTypeRepository);
        verify(holidayDateRepository).findAllByStageType("STAGE_TYPE");
        verifyNoMoreInteractions(holidayDateRepository);
    }
}