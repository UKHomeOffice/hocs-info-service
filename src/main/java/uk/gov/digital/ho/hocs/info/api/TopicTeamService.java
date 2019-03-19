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

import java.util.Optional;
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
        TopicTeam topicTeam = Optional.ofNullable(topicTeamRepository.findByTopicUUIDAndCaseTypeAndStageType(topicUUID, caseType, stageType))
                 .orElse(new TopicTeam(topicUUID, teamUUID, caseType, stageType));
        topicTeam.setResponsibleTeamUUID(teamUUID);
        topicTeamRepository.save(topicTeam);
        log.info("Added team: {} to topic: {}", teamUUID, topicUUID);
        auditClient.addTeamToTopicAudit(topicTeam);

    }

    private void validateTeamTopicStageAndCase(UUID teamUUID, UUID topicUUID, String stageType, String caseType){
        validateTopicUUID(topicUUID);
        validateStageType(stageType);
        validateCaseType(caseType);
        validateTeamUUID(teamUUID);
    }

    private void validateTopicUUID(UUID topicUUID){
        Optional.ofNullable(topicService.getTopic(topicUUID))
                .orElseThrow(() -> new ApplicationExceptions.TopicUpdateException
                        ("Unable to add team to topic, topic {} not found.", topicUUID));
    }
    private void validateStageType(String stageType) {
        Optional.ofNullable(stageTypeService.getStageType(stageType))
                .orElseThrow(() -> new ApplicationExceptions.TopicUpdateException
                        ("Unable to add team to topic, stage type {} not found.", stageType));
    }

    private void validateCaseType(String caseType) {
        Optional.ofNullable(caseTypeService.getCaseType(caseType))
                .orElseThrow(() -> new ApplicationExceptions.TopicUpdateException
                        ("Unable to add team to topic, case type {} not found.", caseType));
    }

    private void validateTeamUUID(UUID teamUUID){
        Team team = Optional.ofNullable(teamService.getTeam(teamUUID))
                .orElseThrow(() -> new ApplicationExceptions.TopicUpdateException
                        ("Unable to add team to topic, topic already has team {} set for this stage and case type.", teamUUID));
        if (!team.isActive()){
            throw new ApplicationExceptions.TopicUpdateException("Unable to add team to topic, team {} is inactive.", teamUUID);
        }
    }
}