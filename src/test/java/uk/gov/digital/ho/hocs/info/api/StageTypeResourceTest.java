package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.StageTypeDto;
import uk.gov.digital.ho.hocs.info.api.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.domain.model.StageTypeEntity;
import uk.gov.digital.ho.hocs.info.domain.model.Team;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StageTypeResourceTest {

    @Mock
    StageTypeService stageTypeService;

    StageTypeResource service;
    Team team = new Team( "Team1" , new HashSet<>());

    @Before
    public void setup() {
        service = new StageTypeResource(stageTypeService);
    }

    @Test
    public void shouldGetStageTypes() {
        StageTypeEntity stage = new StageTypeEntity(1L, UUID.randomUUID(), "stage name", "111","STAGE_TYPE", UUID.randomUUID(),1,1,true,team);
        Set<StageTypeEntity> stages = new HashSet<StageTypeEntity>() {{
            add(stage);
        }};
        when(stageTypeService.getAllStageTypes()).thenReturn(stages);

        ResponseEntity<Set<StageTypeDto>> result = service.getAllStageTypes();

        assertThat(result.getBody().size()).isEqualTo(1);
        StageTypeDto stageTypeDto = (StageTypeDto) result.getBody().toArray()[0];
        assertThat(stageTypeDto.getType()).isEqualTo("STAGE_TYPE");
        assertThat(stageTypeDto.getDisplayName()).isEqualTo("stage name");
        assertThat(stageTypeDto.getShortCode()).isEqualTo("111");
        assertThat(stageTypeDto.getType()).isEqualTo("STAGE_TYPE");

    }

    @Test
    public void shouldGetTeamForStageType() {
        Team team = new Team( "Team1" , true);
        when(stageTypeService.getTeamForStageType("STAGE_TYPE")).thenReturn(team);

        ResponseEntity<TeamDto> result = service.getTeamForStageType("STAGE_TYPE");
        assertThat(result.getBody().getUuid()).isEqualTo(team.getUuid());
        assertThat(result.getBody().getDisplayName()).isEqualTo("Team1");
    }

    @Test
    public void shouldGetDeadlineWarningByStage() {
        LocalDate receivedDate = LocalDate.of(2020,6,5);
        LocalDate caseDeadlineWarningDate = LocalDate.of(2020,6,7);
        LocalDate deadline = LocalDate.of(2020,6,9);
        when(stageTypeService.getDeadlineWarningForStageType("STAGE_TYPE",receivedDate,caseDeadlineWarningDate)).thenReturn(deadline);

        ResponseEntity<LocalDate> response = service.getDeadlineWarningByStage("STAGE_TYPE",receivedDate.toString(),caseDeadlineWarningDate.toString());

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isEqualTo(deadline);
    }
}