package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.data.SimpleMapItem;
import uk.gov.digital.ho.hocs.info.api.dto.AddTeamToTopicDto;
import uk.gov.digital.ho.hocs.info.client.audit.client.AuditClient;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.model.TeamLink;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;
import uk.gov.digital.ho.hocs.info.domain.model.TopicTeam;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamLinkRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.TopicRepository;

import java.util.*;

@Slf4j
@Service
public class TopicTeamService {

    private final TeamLinkRepository teamLinkRepository;

    private final TopicRepository topicRepository;

    private final TeamRepository teamRepository;

    private final TopicService topicService;

    private final TeamService teamService;

    private final CaseTypeService caseTypeService;

    private final StageTypeService stageTypeService;

    private final AuditClient auditClient;

    @Autowired
    public TopicTeamService(TeamLinkRepository teamLinkRepository,
                            TopicRepository topicRepository,
                            TeamRepository teamRepository,
                            TopicService topicService,
                            TeamService teamService,
                            CaseTypeService caseTypeService,
                            StageTypeService stageTypeService,
                            AuditClient auditClient) {
        this.topicService = topicService;
        this.topicRepository = topicRepository;
        this.teamRepository = teamRepository;
        this.teamLinkRepository = teamLinkRepository;
        this.teamService = teamService;
        this.caseTypeService = caseTypeService;
        this.stageTypeService = stageTypeService;
        this.auditClient = auditClient;
    }

    public Set<TopicTeam> getTopicsByCaseTypeWithTeams(String caseType) {
        log.debug("Requesting all topics for case type {}", caseType);
        List<Topic> topics = topicRepository.findTopicsByCaseType(caseType);
        Set<TopicTeam> topicTeams = new HashSet<>();
        topics.forEach(topic -> {
            Set<TeamLink> teamLinks = teamLinkRepository.findAllByLinkValueAndLinkType(topic.getUuid().toString(),
                "TOPIC");
            Set<Team> teams = new HashSet<>();
            teamLinks.forEach(teamLink -> {
                teams.add(teamRepository.findByUuid(teamLink.getResponsibleTeamUUID()));
            });
            topicTeams.add(new TopicTeam(topic.getUuid(), topic.getDisplayName(), teams));
        });
        return topicTeams;
    }

    public void addTeamToTopic(UUID topicUUID, UUID teamUUID, AddTeamToTopicDto request) {
        log.debug("Adding team to Topic: {}", topicUUID);
        String stageType = request.getStageType();
        String caseType = request.getCaseType();

        validateTeamTopicStageAndCase(teamUUID, topicUUID, stageType, caseType);
        TeamLink teamLink = Optional.ofNullable(
            teamLinkRepository.findByLinkValueAndLinkTypeAndCaseTypeAndStageType(topicUUID.toString(), "TOPIC",
                caseType, stageType)).orElse(
            new TeamLink(topicUUID.toString(), "TOPIC", teamUUID, caseType, stageType));
        teamLink.setResponsibleTeamUUID(teamUUID);
        teamLinkRepository.save(teamLink);
        log.info("Added team: {} to topic: {}", teamUUID, topicUUID);
        auditClient.addTeamToTopicAudit(teamLink);

    }

    public List<SimpleMapItem> getTopicToTeamMappingByStageType(String stageType) {
        return teamRepository.findTopicToTeamMappingByStageType(stageType);
    }

    private void validateTeamTopicStageAndCase(UUID teamUUID, UUID topicUUID, String stageType, String caseType) {
        validateTopicUUID(topicUUID);
        validateStageType(stageType);
        validateCaseType(caseType);
        validateTeamUUID(teamUUID);
    }

    private void validateTopicUUID(UUID topicUUID) {
        Optional.ofNullable(topicService.getTopic(topicUUID)).orElseThrow(
            () -> new ApplicationExceptions.TopicUpdateException("Unable to add team to topic, topic {} not found.",
                topicUUID));
    }

    private void validateStageType(String stageType) {
        Optional.ofNullable(stageTypeService.getStageType(stageType)).orElseThrow(
            () -> new ApplicationExceptions.TopicUpdateException(
                "Unable to add team to topic, stage type {} not found.", stageType));
    }

    private void validateCaseType(String caseType) {
        Optional.ofNullable(caseTypeService.getCaseType(caseType)).orElseThrow(
            () -> new ApplicationExceptions.TopicUpdateException("Unable to add team to topic, case type {} not found.",
                caseType));
    }

    private void validateTeamUUID(UUID teamUUID) {
        Team team = Optional.ofNullable(teamService.getTeam(teamUUID)).orElseThrow(
            () -> new ApplicationExceptions.TopicUpdateException(
                "Unable to add team to topic, topic already has team {} set for this stage and case type.", teamUUID));
        if (!team.isActive()) {
            throw new ApplicationExceptions.TopicUpdateException("Unable to add team to topic, team {} is inactive.",
                teamUUID);
        }
    }

}