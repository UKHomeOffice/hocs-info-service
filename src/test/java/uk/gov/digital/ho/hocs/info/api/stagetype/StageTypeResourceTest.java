package uk.gov.digital.ho.hocs.info.api.stagetype;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.GetStageTypesResponse;
import uk.gov.digital.ho.hocs.info.api.dto.StageTypeDto;
import uk.gov.digital.ho.hocs.info.api.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.domain.model.StageTypeEntity;
import uk.gov.digital.ho.hocs.info.domain.model.Team;

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
    UUID teamUUID = UUID.randomUUID();
    Team team = new Team( "Team1",teamUUID , new HashSet<>());

    @Before
    public void setup() {
        service = new StageTypeResource(stageTypeService);
    }

    @Test
    public void shouldGetStageTypes() {
        StageTypeEntity stage = new StageTypeEntity(1L, "stage name", "111","STAGE_TYPE","deadline",true,team);
        Set<StageTypeEntity> stages = new HashSet<StageTypeEntity>() {{
            add(stage);
        }};
        when(stageTypeService.getAllStageTypes()).thenReturn(stages);

        ResponseEntity<GetStageTypesResponse> result = service.getStageTypes();

        assertThat(result.getBody().getStageTypes().size()).isEqualTo(1);
        StageTypeDto stageTypeDto = (StageTypeDto) result.getBody().getStageTypes().toArray()[0];
        assertThat(stageTypeDto.getDisplayCode()).isEqualTo("STAGE_TYPE");
        assertThat(stageTypeDto.getDisplayName()).isEqualTo("stage name");
        assertThat(stageTypeDto.getShortCode()).isEqualTo("111");
        assertThat(stageTypeDto.getType()).isEqualTo("STAGE_TYPE");

    }

    @Test
    public void shouldGetTeamForStageType() {
        TeamDto teamDto = new TeamDto( "Team1",teamUUID , true);
        when(stageTypeService.getTeamForStageType("STAGE_TYPE")).thenReturn(teamDto);

        ResponseEntity<TeamDto> result = service.getTeamForStageType("STAGE_TYPE");
        assertThat(result.getBody().getUuid()).isEqualTo(teamUUID);
        assertThat(result.getBody().getDisplayName()).isEqualTo("Team1");

    }
}