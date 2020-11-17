package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateStandardLineDto;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.client.documentclient.DocumentClient;
import uk.gov.digital.ho.hocs.info.client.documentclient.model.ManagedDocumentType;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.StandardLine;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;
import uk.gov.digital.ho.hocs.info.domain.repository.StandardLineRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StandardLineService {

    private final StandardLineRepository standardLineRepository;
    private final DocumentClient documentClient;
    private final CaseworkClient caseworkClient;
    private final TeamService teamService;
    private final TopicService topicService;
    
    @Autowired
    public StandardLineService(
            StandardLineRepository standardLineRepository,
            DocumentClient documentClient,
            CaseworkClient caseworkClient, 
            TeamService teamService,
            TopicService topicService) {
        this.standardLineRepository = standardLineRepository;
        this.documentClient = documentClient;
        this.caseworkClient = caseworkClient;
        this.teamService = teamService;
        this.topicService = topicService;
    }

    @Transactional
    void createStandardLine(String displayName, UUID topicUUID, LocalDate expires, String s3URL) {
        log.debug("Creating Standard Lines - {}", displayName);
        StandardLine standardLine = standardLineRepository.findStandardLinesByTopicAndExpires(topicUUID, LocalDateTime.of(LocalDate.now(), LocalTime.MAX));

        if (standardLine != null) {
            log.debug("Standard Line {} Found for Topic {}, expiring", standardLine.getDisplayName(), topicUUID);
            standardLine.expire();
            standardLineRepository.save(standardLine);
            documentClient.deleteDocument(standardLine.getDocumentUUID());
            log.info("Set Expiry to now for existing Standard Line - {}, id {}", standardLine.getDisplayName(), standardLine.getUuid());
            caseworkClient.clearCachedStandardLineForTopic(topicUUID);
        }

        StandardLine newStandardLine = new StandardLine(displayName, topicUUID, LocalDateTime.of(expires, LocalTime.MAX.minusHours(1)));
        UUID documentUUID = documentClient.createDocument(newStandardLine.getUuid(), displayName, s3URL, ManagedDocumentType.STANDARD_LINE);
        newStandardLine.setDocumentUUID(documentUUID);
        standardLineRepository.save(newStandardLine);
        log.info("Created Standard Line - {}", displayName);
    }

    Set<StandardLine> getActiveStandardLines() {
        log.debug("Getting Active Standard Lines");
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        Set<StandardLine> standardLines = standardLineRepository.findStandardLinesByExpires(endOfDay);
        log.info("Got {} Standard Lines", standardLines.size());
        return standardLines;
    }

    List<StandardLine> getAllStandardLines() {
        log.debug("Getting All Standard Lines");
        List<StandardLine> standardLines = standardLineRepository.findAllStandardLines();
        log.info("Got {} Standard Lines", standardLines.size());
        return standardLines;
    }

    StandardLine getStandardLineForTopic(UUID topicUUID) {
        log.debug("Getting Standard Line for Topic {} ", topicUUID);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        StandardLine standardLine = standardLineRepository.findStandardLinesByTopicAndExpires(topicUUID, endOfDay);
        if (standardLine != null) {
            log.info("Got Standard Line {} for Topic {} ", standardLine.getDisplayName(), topicUUID);
            return standardLine;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Standard Line for Topic: %s, not found!", topicUUID);
        }
    }
    
    List<StandardLine> getStandardLinesForUser(UUID userUUID) {
        List<StandardLine> standardLines = new ArrayList<>();
        
        log.debug("Getting teams for user: {} ", userUUID);
        List<UUID> usersTeamUuids = teamService.getTeamsForUser(userUUID).stream().map(Team::getUuid).collect(Collectors.toList());
        
        if (usersTeamUuids.size() > 0) {
            log.debug("Getting active topics for teams: {} ", usersTeamUuids);
            List<UUID> topicsUuids = topicService.findActiveTopicsForTeams(usersTeamUuids).stream().map(Topic::getUuid).collect(Collectors.toList());
            
            if (topicsUuids.size() > 0) {
                log.debug("Getting standard lines associated for topics: {} ", topicsUuids);
                standardLines = standardLineRepository.findStandardLinesByTopics(topicsUuids);
            }
        }
   
        log.info("Got {} Standard Lines", standardLines.size());
        return standardLines;
    }

    public void expireStandardLine(UUID standardLineUuid) {
        log.debug("About to expire standard line {} ", standardLineUuid);
        StandardLine standardLine = standardLineRepository.findByUuid(standardLineUuid);
        standardLine.expire();
        standardLineRepository.save(standardLine);
        log.debug("Standard line expired {} ", standardLineUuid);
    }

    public void deleteStandardLine(UUID standardLineUuid) {
        log.debug("About to delete standard line {} ", standardLineUuid);
        StandardLine standardLine = standardLineRepository.findByUuid(standardLineUuid);
        standardLineRepository.delete(standardLine);
        documentClient.deleteDocument(standardLine.getDocumentUUID());
        caseworkClient.clearCachedStandardLineForTopic(standardLine.getTopicUUID());
        log.debug("Standard line deleted {} ", standardLineUuid);
    }

    public StandardLine getStandardLine(UUID standardLineUuid) {
        log.debug("About to get standard line {} ", standardLineUuid);
        return standardLineRepository.findByUuid(standardLineUuid);

    }

    public void updateStandardLine(UUID standardLineUuid, UpdateStandardLineDto request) {
        log.debug("About to update standard line {} ", standardLineUuid);
        StandardLine standardLine = standardLineRepository.findByUuid(standardLineUuid);
        standardLine.update(request);
        standardLineRepository.save(standardLine);
        caseworkClient.clearCachedStandardLineForTopic(standardLine.getTopicUUID());
        log.debug("Standard line updated {} ", standardLineUuid);
    }
}
