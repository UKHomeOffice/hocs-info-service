package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.data.SimpleMapItem;
import uk.gov.digital.ho.hocs.info.api.dto.*;
import uk.gov.digital.ho.hocs.info.domain.model.TopicTeam;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TopicTeamResourceTest {

    @Mock
    private TopicTeamService topicTeamService;

    private TopicTeamResource topicTeamResource;

    @Before
    public void setUp() {
        topicTeamResource = new TopicTeamResource(topicTeamService);
    }

    @Test
    public void shouldGetTopicsByCaseTypeWithTeam() {
        when(topicTeamService.getTopicsByCaseTypeWithTeams("TEST")).thenReturn(new HashSet<TopicTeam>());

        ResponseEntity<Set<TopicTeamDto>> response = topicTeamResource.getTopicsByCaseTypeWithTeams("TEST");

        verify(topicTeamService).getTopicsByCaseTypeWithTeams("TEST");
        verifyNoMoreInteractions(topicTeamService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldAddTeamToTopic() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN", "DCU_MIN_MARKUP");
        UUID teamUUID = UUID.randomUUID();
        UUID topicUUID = UUID.randomUUID();

        ResponseEntity response = topicTeamResource.addTeamToTopic(topicUUID, teamUUID, request);
        verify(topicTeamService).addTeamToTopic(topicUUID, teamUUID, request);
        verifyNoMoreInteractions(topicTeamService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getTopicToTeamMappingByStageType() {
        String testStageString = "STAGE_321";

        List<SimpleMapItem> mockItems = List.of(mock(SimpleMapItem.class), mock(SimpleMapItem.class));
        when(topicTeamService.getTopicToTeamMappingByStageType(testStageString)).thenReturn(mockItems);

        ResponseEntity<List<SimpleMapItem>> response = topicTeamResource.getTopicToTeamMappingByStageType(
            testStageString);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);

        verify(topicTeamService).getTopicToTeamMappingByStageType(testStageString);
        verifyNoMoreInteractions(topicTeamService);
    }

}