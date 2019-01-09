package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.domain.model.StageTypeEntity;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.repository.HolidayDateRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.StageTypeRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StageTypeServiceTest {

    @Mock
    private StageTypeRepository stageTypeRepository;

    @Mock
    private HolidayDateRepository holidayDateRepository;

    private StageTypeService service;
    private final UUID teamUUID = UUID.randomUUID();
    private final Team team = new Team( "Team1",teamUUID , new HashSet<>());

    @Before
    public void setup() {
        service = new StageTypeService(stageTypeRepository, holidayDateRepository);
    }

    @Test
    public void shouldGetAllStageTypes() {
        StageTypeEntity stage = new StageTypeEntity(1L, UUID.randomUUID(), "stage name", "111","STAGE_TYPE", UUID.randomUUID(),1,true, team);
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
        StageTypeEntity stage = new StageTypeEntity(1L, UUID.randomUUID(), "stage name", "111","STAGE_TYPE", UUID.randomUUID(),1,true,team);

        when(stageTypeRepository.findByType("STAGE_TYPE")).thenReturn(stage);

        Team result = service.getTeamForStageType("STAGE_TYPE");
        assertThat(result.getUuid()).isEqualTo(teamUUID);
        assertThat(result.getDisplayName()).isEqualTo("Team1");

    }
}