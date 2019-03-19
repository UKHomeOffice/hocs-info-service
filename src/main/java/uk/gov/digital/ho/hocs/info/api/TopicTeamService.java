package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.AddTeamToTopicDto;
import uk.gov.digital.ho.hocs.info.client.auditClient.AuditClient;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.model.TopicTeam;
import uk.gov.digital.ho.hocs.info.domain.repository.TopicTeamRepository;

import java.util.UUID;

@Slf4j
@Service
public class TopicTeamService {

    private final TopicTeamRepository topicTeamRepository;
    private final TopicService topicService;
    private final TeamService teamService;
    private final CaseTypeService caseTypeService;
    private final StageTypeService stageTypeService;
    private final AuditClient auditClient;

    @Autowired
    public TopicTeamService(TopicTeamRepository topicTeamRepository,
                            TopicService topicService,
                            TeamService teamService,
                            CaseTypeService caseTypeService,
                            StageTypeService stageTypeService,
                            AuditClient auditClient) {
        this.topicService = topicService;
        this.topicTeamRepository = topicTeamRepository;
        this.teamService = teamService;
        this.caseTypeService = caseTypeService;
        this.stageTypeService = stageTypeService;
        this.auditClient = auditClient;
    }

    public void addTeamToTopic(UUID topicUUID, UUID teamUUID, AddTeamToTopicDto request) {
        log.debug("Adding team to Topic: {}", topicUUID);
        String stageType = request.getStageType();
        String caseType = request.getCaseType();
        validateTeamTopicStageAndCase(teamUUID, topicUUID, stageType, caseType);
        if (topicTeamRepository.findByTopicUUIDAndCaseTypeAndStageType(topicUUID, caseType, stageType) != null) {
            throw new ApplicationExceptions.TopicUpdateException(
                    "Unable to add team to topic, topic already has a team set for this stage and case type.");
        } else {
            TopicTeam topicTeam = topicTeamRepository.save(new TopicTeam(topicUUID, teamUUID, caseType, stageType));
            log.info("Added team: {} to topic: {}", teamUUID, topicUUID);
            auditClient.addTeamToTopicAudit(topicTeam);
        }
    }

    public void updateTeamForTopic(UUID topicUUID, UUID teamUUID, AddTeamToTopicDto request) {
        log.debug("Updating team for Topic: {}", topicUUID);
        String stageType = request.getStageType();
        String caseType = request.getCaseType();

        validateTeamTopicStageAndCase(teamUUID, topicUUID, stageType, caseType);
        TopicTeam topicTeam = topicTeamRepository.findByTopicUUIDAndCaseTypeAndStageType(topicUUID, caseType, stageType);
        if (topicTeam != null) {
            UUID oldTeamUUID = topicTeam.getResponsibleTeamUUID();
            topicTeam.setResponsibleTeamUUID(teamUUID);
            topicTeamRepository.save(topicTeam);
            log.info("Updated topic: {} with team : {}", topicUUID, teamUUID);
            auditClient.updateTeamForTopicAudit(topicTeam, oldTeamUUID);
        } else {
            throw new ApplicationExceptions.TopicUpdateException("Unable update topic team, topic has no team to update for this stage and case type.", topicUUID);
        }
    }

    private boolean validateTeamTopicStageAndCase(UUID teamUUID, UUID topicUUID, String stageType, String caseType){
        if (topicService.getTopic(topicUUID) == null) {
            throw new ApplicationExceptions.TopicUpdateException("Unable to add team to topic, topic {} not found.", topicUUID);
        } else {
            if (stageTypeService.getStageType(stageType) == null){
                throw new ApplicationExceptions.TopicUpdateException("Unable to add team to topic, stage type{} not found.", stageType);
            } else {
                if (caseTypeService.getCaseType(caseType) == null) {
                    throw new ApplicationExceptions.TopicUpdateException("Unable to add team to topic, case type {} not found.", caseType);
                } else {
                    Team team = teamService.getTeam(teamUUID);
                    if (team == null) {
                        throw new ApplicationExceptions.TopicUpdateException("Unable to add team to topic, team {} not found.", teamUUID);
                    } else {
                        if (!team.isActive()){
                            throw new ApplicationExceptions.TopicUpdateException("Unable to add team to topic, team {} is inactive.", teamUUID);
                        }
                    }
                }
            }
        }
        return true;
    }
}