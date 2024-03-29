package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.data.SimpleMapItem;
import uk.gov.digital.ho.hocs.info.api.dto.AddTeamToTopicDto;
import uk.gov.digital.ho.hocs.info.client.audit.client.AuditClient;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.*;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamLinkRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.TopicRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TopicTeamServiceTest {

    private TopicTeamService topicTeamService;

    @Mock
    private TeamLinkRepository teamLinkRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TopicService topicService;

    @Mock
    private TeamService teamService;

    @Mock
    private CaseTypeService caseTypeService;

    @Mock
    private StageTypeService stageTypeService;

    @Mock
    private AuditClient auditClient;

    @Before
    public void setUp() {
        this.topicTeamService = new TopicTeamService(teamLinkRepository, topicRepository, teamRepository, topicService,
            teamService, caseTypeService, stageTypeService, auditClient);
    }

    @Test
    public void getTopicsByCaseTypeWithTeams() {
        Topic topic = new Topic(0, "0", UUID.randomUUID(), UUID.randomUUID(), true);
        List<Topic> topics = Arrays.asList(topic);
        when(topicRepository.findTopicsByCaseType("TEST")).thenReturn(topics);
        Team team1 = new Team("Team1", true);
        when(teamRepository.findByUuid(team1.getUuid())).thenReturn(team1);
        Team team2 = new Team("Team2", true);
        when(teamRepository.findByUuid(team2.getUuid())).thenReturn(team2);
        TeamLink teamLink1 = new TeamLink(topic.getUuid().toString(), "TOPIC", team1.getUuid(), "TEST", "TEST");
        TeamLink teamLink2 = new TeamLink(topic.getUuid().toString(), "TOPIC", team2.getUuid(), "TEST", "TEST");
        Set<TeamLink> teamLinks = Set.of(teamLink1, teamLink2);
        when(teamLinkRepository.findAllByLinkValueAndLinkType(topic.getUuid().toString(), "TOPIC")).thenReturn(
            teamLinks);

        Set<TopicTeam> result = topicTeamService.getTopicsByCaseTypeWithTeams("TEST");

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.iterator().next().getTeams().size()).isEqualTo(2);
        verify(topicRepository).findTopicsByCaseType("TEST");
        verify(teamRepository).findByUuid(team1.getUuid());
        verify(teamRepository).findByUuid(team2.getUuid());
        verify(teamLinkRepository).findAllByLinkValueAndLinkType(topic.getUuid().toString(), "TOPIC");
        verifyNoMoreInteractions(topicRepository);
        verifyNoMoreInteractions(teamRepository);
        verifyNoMoreInteractions(teamLinkRepository);
    }

    @Test
    public void shouldSuccessfullyAddTeamToTopic() {
        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN", "DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();

        when(teamService.getTeam(teamUUID)).thenReturn(new Team("name", true));
        when(stageTypeService.getStageType("DCU_MIN_MARKUP")).thenReturn(new StageTypeEntity());
        when(caseTypeService.getCaseType("MIN")).thenReturn(new CaseType());
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());

        topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);

        verify(teamLinkRepository).save(any());
        verify(teamLinkRepository).findByLinkValueAndLinkTypeAndCaseTypeAndStageType(any(), eq("TOPIC"), any(), any());
        verifyNoMoreInteractions(teamLinkRepository);
    }

    @Test(expected = ApplicationExceptions.TopicUpdateException.class)
    public void shouldThrowExceptionWhenAddingNonExistentTeamToTopic() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN", "DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();
        when(teamService.getTeam(teamUUID)).thenReturn(null);
        when(stageTypeService.getStageType("DCU_MIN_MARKUP")).thenReturn(new StageTypeEntity());
        when(caseTypeService.getCaseType("MIN")).thenReturn(new CaseType());
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());

        topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
    }

    @Test
    public void shouldNotAddTeamWhenAddingNonExistentTeamToTopic() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN", "DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();
        when(teamService.getTeam(teamUUID)).thenReturn(null);
        when(stageTypeService.getStageType("DCU_MIN_MARKUP")).thenReturn(new StageTypeEntity());
        when(caseTypeService.getCaseType("MIN")).thenReturn(new CaseType());
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());

        try {
            topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
        } catch (ApplicationExceptions.TopicUpdateException e) {
            // Do nothing.
        }

        verifyNoMoreInteractions(teamLinkRepository);
    }

    @Test(expected = ApplicationExceptions.TopicUpdateException.class)
    public void shouldThrowExceptionWhenAddingInactiveTeamToTopic() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN", "DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();
        when(teamService.getTeam(teamUUID)).thenReturn(new Team("name", false));
        when(stageTypeService.getStageType("DCU_MIN_MARKUP")).thenReturn(new StageTypeEntity());
        when(caseTypeService.getCaseType("MIN")).thenReturn(new CaseType());
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());

        topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
    }

    @Test
    public void shouldNotAddTeamWhenAddingInactiveTeamToTopic() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN", "DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();
        when(teamService.getTeam(teamUUID)).thenReturn(new Team("name", false));
        when(stageTypeService.getStageType("DCU_MIN_MARKUP")).thenReturn(new StageTypeEntity());
        when(caseTypeService.getCaseType("MIN")).thenReturn(new CaseType());
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());

        try {
            topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
        } catch (ApplicationExceptions.TopicUpdateException e) {
            // Do nothing.
        }

        verifyNoMoreInteractions(teamLinkRepository);
    }

    @Test(expected = ApplicationExceptions.TopicUpdateException.class)
    public void shouldThrowExceptionWhenAddingTeamToNonexistentTopic() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN", "DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();

        topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
    }

    @Test
    public void shouldNotAddTeamWhenAddingTeamToNonexistentTopic() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN", "DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();

        try {
            topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
        } catch (ApplicationExceptions.TopicUpdateException e) {
            // Do nothing.
        }

        verifyNoMoreInteractions(teamLinkRepository);
    }

    @Test(expected = ApplicationExceptions.TopicUpdateException.class)
    public void shouldThrowExceptionWhenAddingTeamToTopicAndCaseTypeIsInvalid() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN", "DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();

        when(stageTypeService.getStageType("DCU_MIN_MARKUP")).thenReturn(new StageTypeEntity());
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());

        topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
    }

    @Test
    public void shouldNotAddTeamWhenAddingTeamToTopicAndCaseTypeIsInvalid() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN", "DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();
        when(stageTypeService.getStageType("DCU_MIN_MARKUP")).thenReturn(new StageTypeEntity());
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());
        try {
            topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
        } catch (ApplicationExceptions.TopicUpdateException e) {
            // Do nothing.
        }

        verifyNoMoreInteractions(teamLinkRepository);
    }

    @Test(expected = ApplicationExceptions.TopicUpdateException.class)
    public void shouldThrowExceptionWhenAddingTeamToTopicAndStageTypeIsInvalid() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN", "DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());

        topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
    }

    @Test
    public void shouldNotAddTeamWhenAddingTeamToTopicAndStageTypeIsInvalid() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN", "DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());
        try {
            topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
        } catch (ApplicationExceptions.TopicUpdateException e) {
            // Do nothing.
        }

        verifyNoMoreInteractions(teamLinkRepository);
    }

    @Test
    public void shouldUpdateTeamWhenAddingTeamToTopicAndTopicAlreadyHasATeam() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN", "DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();

        when(teamService.getTeam(teamUUID)).thenReturn(new Team("name", true));
        when(stageTypeService.getStageType("DCU_MIN_MARKUP")).thenReturn(new StageTypeEntity());
        when(caseTypeService.getCaseType("MIN")).thenReturn(new CaseType());
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());
        when(teamLinkRepository.findByLinkValueAndLinkTypeAndCaseTypeAndStageType(any(), eq("TOPIC"), any(),
            any())).thenReturn(new TeamLink());

        topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);

        verify(teamLinkRepository).findByLinkValueAndLinkTypeAndCaseTypeAndStageType(any(), eq("TOPIC"), any(), any());
        verify(teamLinkRepository).save(any());
        verifyNoMoreInteractions(teamLinkRepository);
    }

    @Test
    public void shouldAuditAddTeamToTopic() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN", "DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();

        when(teamService.getTeam(teamUUID)).thenReturn(new Team("name", true));
        when(stageTypeService.getStageType("DCU_MIN_MARKUP")).thenReturn(new StageTypeEntity());
        when(caseTypeService.getCaseType("MIN")).thenReturn(new CaseType());
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());

        topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);

        verify(auditClient).addTeamToTopicAudit(any());
        verifyNoMoreInteractions(auditClient);
    }

    @Test
    public void getTopicToTeamMappingByStageType() {
        String testStageString = "STAGE_321";

        List<SimpleMapItem> mockItems = List.of(mock(SimpleMapItem.class), mock(SimpleMapItem.class));
        when(teamRepository.findTopicToTeamMappingByStageType(testStageString)).thenReturn(mockItems);

        List<SimpleMapItem> results = topicTeamService.getTopicToTeamMappingByStageType(testStageString);

        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(2);

        verify(teamRepository).findTopicToTeamMappingByStageType(testStageString);
        verifyNoMoreInteractions(teamRepository);
    }

}
