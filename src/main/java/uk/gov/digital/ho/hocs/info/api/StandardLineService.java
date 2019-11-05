package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.client.documentClient.DocumentClient;
import uk.gov.digital.ho.hocs.info.client.documentClient.model.ManagedDocumentType;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.StandardLine;
import uk.gov.digital.ho.hocs.info.domain.repository.StandardLineRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class StandardLineService {

    private final StandardLineRepository standardLineRepository;
    private final DocumentClient documentClient;
    private final CaseworkClient caseworkClient;

    @Autowired
    public StandardLineService(
            StandardLineRepository standardLineRepository,
            DocumentClient documentClient,
            CaseworkClient caseworkClient) {
        this.standardLineRepository = standardLineRepository;
        this.documentClient = documentClient;
        this.caseworkClient = caseworkClient;
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

        StandardLine newStandardLine = new StandardLine(displayName, topicUUID, LocalDateTime.of(expires, LocalTime.MAX));
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
}
