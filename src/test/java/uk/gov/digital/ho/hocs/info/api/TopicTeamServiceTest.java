package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.AddTeamToTopicDto;
import uk.gov.digital.ho.hocs.info.client.auditClient.AuditClient;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.*;
import uk.gov.digital.ho.hocs.info.domain.repository.TopicTeamRepository;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TopicTeamServiceTest {

    private TopicTeamService topicTeamService;

    @Mock
    private TopicTeamRepository topicTeamRepository;
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
        this.topicTeamService = new TopicTeamService (topicTeamRepository,
                     topicService,
                     teamService,
                     caseTypeService,
                     stageTypeService,
                     auditClient);
    }

    @Test
    public void shouldSuccessfullyAddTeamToTopic() {
        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN","DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();

        when(teamService.getTeam(teamUUID)).thenReturn(new Team("name", true));
        when(stageTypeService.getStageType("DCU_MIN_MARKUP")).thenReturn(new StageTypeEntity());
        when(caseTypeService.getCaseType("MIN")).thenReturn(new CaseType());
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());

        topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);


        verify(topicTeamRepository, times(1)).save(any());
        verify(topicTeamRepository, times(1)).findByTopicUUIDAndCaseTypeAndStageType(any(), any(), any());
        verifyNoMoreInteractions(topicTeamRepository);
    }

    @Test (expected = ApplicationExceptions.TopicUpdateException.class)
    public void shouldThrowExceptionWhenAddingNonExistentTeamToTopic() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN","DCU_MIN_MARKUP");
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

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN","DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();
        when(teamService.getTeam(teamUUID)).thenReturn(null);
        when(stageTypeService.getStageType("DCU_MIN_MARKUP")).thenReturn(new StageTypeEntity());
        when(caseTypeService.getCaseType("MIN")).thenReturn(new CaseType());
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());

        try { topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
        } catch (ApplicationExceptions.TopicUpdateException e) {
            // Do nothing.
        }

        verifyZeroInteractions(topicTeamRepository);
    }

    @Test (expected = ApplicationExceptions.TopicUpdateException.class)
    public void shouldThrowExceptionWhenAddingInactiveTeamToTopic() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN","DCU_MIN_MARKUP");
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

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN","DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();
        when(teamService.getTeam(teamUUID)).thenReturn(new Team("name", false));
        when(stageTypeService.getStageType("DCU_MIN_MARKUP")).thenReturn(new StageTypeEntity());
        when(caseTypeService.getCaseType("MIN")).thenReturn(new CaseType());
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());

        try { topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
        } catch (ApplicationExceptions.TopicUpdateException e) {
            // Do nothing.
        }

        verifyZeroInteractions(topicTeamRepository);
    }

    @Test (expected = ApplicationExceptions.TopicUpdateException.class)
    public void shouldThrowExceptionWhenAddingTeamToNonexistentTopic() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN","DCU_MIN_MARKUP");
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

        verifyZeroInteractions(topicTeamRepository);
    }

    @Test (expected = ApplicationExceptions.TopicUpdateException.class)
    public void shouldThrowExceptionWhenAddingTeamToTopicAndCaseTypeIsInvalid() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN","DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();

        when(stageTypeService.getStageType("DCU_MIN_MARKUP")).thenReturn(new StageTypeEntity());
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());

        topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
    }

    @Test
    public void shouldNotAddTeamWhenAddingTeamToTopicAndCaseTypeIsInvalid() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN","DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();
        when(stageTypeService.getStageType("DCU_MIN_MARKUP")).thenReturn(new StageTypeEntity());
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());
        try { topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
        } catch (ApplicationExceptions.TopicUpdateException e) {
            // Do nothing.
        }

        verifyZeroInteractions(topicTeamRepository);
    }

    @Test (expected = ApplicationExceptions.TopicUpdateException.class)
    public void shouldThrowExceptionWhenAddingTeamToTopicAndStageTypeIsInvalid() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN","DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());

        topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
    }

    @Test
    public void shouldNotAddTeamWhenAddingTeamToTopicAndStageTypeIsInvalid() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN","DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());
        try { topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
        } catch (ApplicationExceptions.TopicUpdateException e) {
            // Do nothing.
        }

        verifyZeroInteractions(topicTeamRepository);
    }

    @Test
    public void shouldUpdateTeamWhenAddingTeamToTopicAndTopicAlreadyHasATeam() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN","DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();

        when(teamService.getTeam(teamUUID)).thenReturn(new Team("name", true));
        when(stageTypeService.getStageType("DCU_MIN_MARKUP")).thenReturn(new StageTypeEntity());
        when(caseTypeService.getCaseType("MIN")).thenReturn(new CaseType());
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());
        when(topicTeamRepository.findByTopicUUIDAndCaseTypeAndStageType(
                any(), any(), any())).thenReturn(new TopicTeam());

        topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);

        verify(topicTeamRepository, times(1)).findByTopicUUIDAndCaseTypeAndStageType(any(), any(), any());
        verify(topicTeamRepository, times(1)).save(any());
        verifyNoMoreInteractions(topicTeamRepository);
    }


    @Test
    public void shouldAuditAddTeamToTopic(){

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN","DCU_MIN_MARKUP");
        UUID topicUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();

        when(teamService.getTeam(teamUUID)).thenReturn(new Team("name", true));
        when(stageTypeService.getStageType("DCU_MIN_MARKUP")).thenReturn(new StageTypeEntity());
        when(caseTypeService.getCaseType("MIN")).thenReturn(new CaseType());
        when(topicService.getTopic(topicUUID)).thenReturn(new Topic());

        topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);

        verify(auditClient, times(1)).addTeamToTopicAudit(any());
        verifyNoMoreInteractions(auditClient);
    }
}