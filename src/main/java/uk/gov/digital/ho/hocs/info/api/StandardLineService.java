package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.client.documentClient.DocumentClient;
import uk.gov.digital.ho.hocs.info.client.documentClient.model.ManagedDocumentType;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.StandardLine;
import uk.gov.digital.ho.hocs.info.domain.repository.StandardLineRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Service
@Slf4j
public class StandardLineService {

    private final StandardLineRepository standardLineRepository;
    private final DocumentClient documentClient;

    private final LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

    @Autowired
    public StandardLineService(
            StandardLineRepository standardLineRepository,
            DocumentClient documentClient) {
        this.standardLineRepository = standardLineRepository;
        this.documentClient = documentClient;
    }

    void createStandardLine(String displayName, UUID topicUUID, LocalDate expires, String s3URL) {
        log.debug("Creating Standard Lines - {}", displayName);
        StandardLine standardLine = standardLineRepository.findStandardLinesByTopicAndExpires(topicUUID, LocalDateTime.of(LocalDate.now(), LocalTime.MAX));

        if (standardLine != null) {
            log.debug("Standard Line {} Found for Topic {}, expiring", standardLine.getDisplayName(), topicUUID);
            standardLine.expire();
            standardLineRepository.save(standardLine);
            documentClient.deleteDocument(standardLine.getUuid());
            log.info("Set Expiry to now for existing Standard Line - {}, id {}", standardLine.getDisplayName(), standardLine.getUuid());
        }

        StandardLine newStandardLine = new StandardLine(displayName, topicUUID, LocalDateTime.of(expires, LocalTime.MAX));
        standardLineRepository.save(newStandardLine);

        UUID documentUUID = documentClient.createDocument(newStandardLine.getUuid(), displayName, ManagedDocumentType.STANDARD_LINE);

        documentClient.processDocument(ManagedDocumentType.STANDARD_LINE, documentUUID, s3URL);
        log.info("Created Standard Line - {}", displayName);
    }

    StandardLine getStandardLineForTopic(UUID topicUUID) {
        log.debug("Getting Standard Line for Topic {} ", topicUUID);
        StandardLine standardLine = standardLineRepository.findStandardLinesByTopicAndExpires(topicUUID, endOfDay);
        if (standardLine != null) {
            log.info("Got Standard Line {} for Topic {} ", standardLine.getDisplayName(), topicUUID);
            return standardLine;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Standard Line for Topic: %s, not found!", topicUUID);
        }
    }
}
