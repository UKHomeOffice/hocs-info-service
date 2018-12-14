package uk.gov.digital.ho.hocs.info.stagetype;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.entities.StageTypeEntity;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.repositories.StageTypeRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StageTypeServiceTest {

    @Mock
    StageTypeRepository stageTypeRepository;

    StageTypeService service;
    UUID teamUUID = UUID.randomUUID();
    Team team = new Team( "Team1",teamUUID , new HashSet<>());

    @Before
    public void setup() {
        service = new StageTypeService(stageTypeRepository);
    }

    @Test
    public void shouldGetAllStageTypes() {
        StageTypeEntity stage = new StageTypeEntity(1L, "stage name", "111","STAGE_TYPE","deadline",true,team);
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
        StageTypeEntity stage = new StageTypeEntity(1L, "stage name", "111","STAGE_TYPE","deadline",true,team);

        when(stageTypeRepository.findByType("STAGE_TYPE")).thenReturn(stage);

        TeamDto result = service.getTeamForStageType("STAGE_TYPE");
        assertThat(result.getUuid()).isEqualTo(teamUUID);
        assertThat(result.getDisplayName()).isEqualTo("Team1");

    }
}