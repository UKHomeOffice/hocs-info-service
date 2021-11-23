package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateStandardLineDto;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.client.documentclient.DocumentClient;
import uk.gov.digital.ho.hocs.info.client.documentclient.model.ManagedDocumentType;
import uk.gov.digital.ho.hocs.info.api.dto.CreateStandardLineDocumentDto;
import uk.gov.digital.ho.hocs.info.domain.model.StandardLine;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;
import uk.gov.digital.ho.hocs.info.domain.repository.StandardLineRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StandardLineServiceTest {

    @Mock
    private StandardLineRepository standardLineRepository;

    @Mock
    private DocumentClient documentClient;

    @Mock
    private CaseworkClient caseworkClient;

    @Mock
    private TeamService teamService;

    @Mock
    private TopicService topicService;

    private StandardLineService standardLineService;

    private static final UUID uuid = UUID.randomUUID();
    private static final UUID standardLineUUID = UUID.randomUUID();
    private static final UUID topicUUID = UUID.randomUUID();
    private static final UUID documentUUID = UUID.randomUUID();
    private static final UUID userUUID = UUID.randomUUID();
    private static final LocalDateTime END_OF_DAY = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    private static final String DISPLAY_NAME = "dn";
    private static final UUID NEW_DOCUMENT_UUID = UUID.randomUUID();

    @Before
    public void setUp() {
        this.standardLineService = new StandardLineService(standardLineRepository, documentClient, caseworkClient, teamService, topicService);
    }

    @Test
    public void shouldReturnStandardLine(){
        when(standardLineRepository.findStandardLinesByExpires(END_OF_DAY)).thenReturn(Set.of(new StandardLine()));
        standardLineService.getActiveStandardLines();
        verify(standardLineRepository).findStandardLinesByExpires(END_OF_DAY);
        verifyNoMoreInteractions(standardLineRepository);
    }

    @Test
    public void shouldReturnStandardLineForPrimaryTopic(){
        when(standardLineRepository.findStandardLinesByTopicAndExpires(uuid, END_OF_DAY)).thenReturn(new StandardLine());
        standardLineService.getStandardLineForTopic(uuid);
        verify(standardLineRepository).findStandardLinesByTopicAndExpires(uuid, END_OF_DAY);
        verifyNoMoreInteractions(standardLineRepository);
    }

    @Test
    public void shouldCreateNewStandardLine(){
        CreateStandardLineDocumentDto request = new CreateStandardLineDocumentDto(DISPLAY_NAME,"URL",uuid,LocalDate.now().plusDays(1));

        when(documentClient.createDocument(any(UUID.class), eq(request.getDisplayName()), eq("URL"), eq(ManagedDocumentType.STANDARD_LINE))).thenReturn(NEW_DOCUMENT_UUID);
        when(standardLineRepository.findStandardLinesByTopicAndExpires(uuid, END_OF_DAY)).thenReturn(null);

        standardLineService.createStandardLine(request.getDisplayName(), request.getTopicUUID(), request.getExpires(), request.getS3UntrustedUrl());

        verify(documentClient).createDocument(any(UUID.class), eq(DISPLAY_NAME), eq("URL"), eq(ManagedDocumentType.STANDARD_LINE));
        verify(standardLineRepository).findStandardLinesByTopicAndExpires(uuid, END_OF_DAY);
        verify(standardLineRepository).save(any());
        verifyNoMoreInteractions(standardLineRepository);
        verifyNoMoreInteractions(documentClient);
    }

    @Test
    public void shouldCreateStandardLineExpiringPrevious(){
        CreateStandardLineDocumentDto request = new CreateStandardLineDocumentDto(DISPLAY_NAME,"URL",uuid,LocalDate.now().plusDays(1));

        StandardLine standardLine = new StandardLine(DISPLAY_NAME, uuid, END_OF_DAY);
        standardLine.setDocumentUUID(UUID.randomUUID());

        when(documentClient.createDocument(any(UUID.class), eq(request.getDisplayName()), eq("URL"), eq(ManagedDocumentType.STANDARD_LINE))).thenReturn(NEW_DOCUMENT_UUID);
        when(standardLineRepository.findStandardLinesByTopicAndExpires(uuid , END_OF_DAY)).thenReturn(standardLine);
      
        standardLineService.createStandardLine(request.getDisplayName(), request.getTopicUUID(), request.getExpires(), request.getS3UntrustedUrl());

        verify(documentClient).createDocument(any(UUID.class), eq(DISPLAY_NAME), eq("URL"), eq(ManagedDocumentType.STANDARD_LINE));
        verify(standardLineRepository).findStandardLinesByTopicAndExpires(uuid, END_OF_DAY);
        verify(standardLineRepository, times(2)).save(any());
        verify(documentClient).deleteDocument(standardLine.getDocumentUUID());
        verifyNoMoreInteractions(standardLineRepository);
        verifyNoMoreInteractions(documentClient);
        verify(caseworkClient).clearCachedStandardLineForTopic(uuid);
        verifyNoMoreInteractions(caseworkClient);
    }

    @Test
    public void getAllStandardLines(){
        List<StandardLine> standardLines = List.of(new StandardLine("DisplayName", uuid, LocalDateTime.now()));

        when(standardLineRepository.findAllStandardLines()).thenReturn(standardLines);

        List<StandardLine> result = standardLineService.getAllStandardLines();
        verify(standardLineRepository).findAllStandardLines();
        checkNoMoreInteractions();
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getDisplayName()).isEqualTo("DisplayName");
        assertThat(result.get(0).getTopicUUID()).isEqualTo(uuid);
    }

    @Test
    public void getStandardLine(){
        when(standardLineRepository.findByUuid(standardLineUUID)).thenReturn(new StandardLine("DisplayName", uuid, LocalDateTime.now()));

        StandardLine result = standardLineService.getStandardLine(standardLineUUID);
        verify(standardLineRepository).findByUuid(standardLineUUID);
        checkNoMoreInteractions();
        assertThat(result).isNotNull();
        assertThat(result.getDisplayName()).isEqualTo("DisplayName");
        assertThat(result.getTopicUUID()).isEqualTo(uuid);
    }

    @Test
    public void expireStandardLine(){
        StandardLine standardLineMock = mock(StandardLine.class);
        when(standardLineRepository.findByUuid(standardLineUUID)).thenReturn(standardLineMock);

        standardLineService.expireStandardLine(standardLineUUID);

        verify(standardLineMock).expire();
        verify(standardLineRepository).findByUuid(standardLineUUID);
        verify(standardLineRepository).save(standardLineMock);
        checkNoMoreInteractions();

    }

    @Test
    public void deleteStandardLine(){
        StandardLine standardLineMock = mock(StandardLine.class);
        when(standardLineMock.getTopicUUID()).thenReturn(topicUUID);
        when(standardLineMock.getDocumentUUID()).thenReturn(documentUUID);
        when(standardLineRepository.findByUuid(standardLineUUID)).thenReturn(standardLineMock);

        standardLineService.deleteStandardLine(standardLineUUID);

        verify(standardLineMock).getTopicUUID();
        verify(standardLineMock).getDocumentUUID();
        verify(standardLineRepository).findByUuid(standardLineUUID);
        verify(standardLineRepository).delete(standardLineMock);
        verify(documentClient).deleteDocument(documentUUID);
        verify(caseworkClient).clearCachedStandardLineForTopic(topicUUID);

        checkNoMoreInteractions();

    }

    @Test
    public void updateStandardLine(){
        StandardLine standardLineMock = mock(StandardLine.class);
        when(standardLineMock.getTopicUUID()).thenReturn(topicUUID);
        UpdateStandardLineDto updateStandardLineDto = new UpdateStandardLineDto("NewDN", LocalDate.now().plusDays(10));
        when(standardLineRepository.findByUuid(standardLineUUID)).thenReturn(standardLineMock);

        standardLineService.updateStandardLine(standardLineUUID, updateStandardLineDto);

        verify(standardLineMock).update(updateStandardLineDto);
        verify(standardLineMock).getTopicUUID();
        verify(standardLineRepository).findByUuid(standardLineUUID);
        verify(standardLineRepository).save(standardLineMock);
        verify(caseworkClient).clearCachedStandardLineForTopic(topicUUID);
        checkNoMoreInteractions();

    }

    @Test
    public void getStandardLinesForUser(){
        List<StandardLine> standardLines = List.of(new StandardLine("DisplayName", uuid, LocalDateTime.now()));
        Team team = new Team("Test", true);
        Topic topic = new Topic("Topic Test", UUID.randomUUID());
        Set<Team> teamsSet = Set.of(team);
        List<Topic> topics = List.of(topic);

        when(teamService.getTeamsForUser(userUUID)).thenReturn(teamsSet);
        when(topicService.findActiveTopicsForTeams(List.of(team.getUuid()))).thenReturn(topics);
        when(standardLineRepository.findStandardLinesByTopics(List.of(topic.getUuid()))).thenReturn(standardLines);

        List<StandardLine> result = standardLineService.getStandardLinesForUser(userUUID);
        verify(teamService).getTeamsForUser(userUUID);
        verify(topicService).findActiveTopicsForTeams(List.of(team.getUuid()));
        verify(standardLineRepository).findStandardLinesByTopics(List.of(topic.getUuid()));
        checkNoMoreInteractions();
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getDisplayName()).isEqualTo("DisplayName");
        assertThat(result.get(0).getTopicUUID()).isEqualTo(uuid);
    }

    @Test
    public void getStandardLinesForUser_NoTeams_ReturnsEmptyList(){
        Set<Team> teamSet = new HashSet<>();

        when(teamService.getTeamsForUser(userUUID)).thenReturn(teamSet);

        List<StandardLine> result = standardLineService.getStandardLinesForUser(userUUID);
        verify(teamService).getTeamsForUser(userUUID);
        checkNoMoreInteractions();
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void getStandardLinesForUser_TeamWithNoTopic_ReturnsEmptyList(){
        Team team = new Team("Test", true);
        Set<Team> teamsSet = Set.of(team);

        when(teamService.getTeamsForUser(userUUID)).thenReturn(teamsSet);

        List<StandardLine> result = standardLineService.getStandardLinesForUser(userUUID);
        verify(teamService).getTeamsForUser(userUUID);
        verify(topicService).findActiveTopicsForTeams(List.of(team.getUuid()));
        checkNoMoreInteractions();
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void getStandardLinesForUser_TeamTopicNoStandardLines_ReturnsEmptyList(){
        List<StandardLine> standardLines = new ArrayList<>();
        Team team = new Team("Test", true);
        Topic topic = new Topic("Topic Test", UUID.randomUUID());
        Set<Team> teamsSet = Set.of(team);
        List<Topic> topics = List.of(topic);

        when(teamService.getTeamsForUser(userUUID)).thenReturn(teamsSet);
        when(topicService.findActiveTopicsForTeams(List.of(team.getUuid()))).thenReturn(topics);
        when(standardLineRepository.findStandardLinesByTopics(List.of(topic.getUuid()))).thenReturn(standardLines);

        List<StandardLine> result = standardLineService.getStandardLinesForUser(userUUID);
        verify(teamService).getTeamsForUser(userUUID);
        verify(topicService).findActiveTopicsForTeams(List.of(team.getUuid()));
        verify(standardLineRepository).findStandardLinesByTopics(List.of(topic.getUuid()));
        checkNoMoreInteractions();
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    private void checkNoMoreInteractions(){
        verifyNoMoreInteractions(standardLineRepository, documentClient, caseworkClient, teamService, topicService);
    }
}
