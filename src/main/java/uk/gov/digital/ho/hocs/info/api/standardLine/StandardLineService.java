package uk.gov.digital.ho.hocs.info.api.standardLine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.dto.GetCaseworkCaseDataResponse;
import uk.gov.digital.ho.hocs.info.client.documentClient.DocumentClient;
import uk.gov.digital.ho.hocs.info.client.documentClient.model.ManagedDocumentType;
import uk.gov.digital.ho.hocs.info.api.dto.CreateStandardLineDocumentDto;
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
    private final UUID SL_EXT_REF_UUID = UUID.fromString("77777777-7777-7777-7777-777777777777");
    LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    private  final CaseworkClient caseworkClient;

    @Autowired
    public StandardLineService(
            StandardLineRepository standardLineRepository,
            DocumentClient documentClient,
            CaseworkClient caseworkClient) {
        this.standardLineRepository = standardLineRepository;
        this.documentClient = documentClient;
        this.caseworkClient = caseworkClient;
    }

    public StandardLine getStandardLines(UUID topicUUID) {
        StandardLine standardLine = standardLineRepository.findStandardLinesByTopicAndExpires(topicUUID, endOfDay);
        if (standardLine != null) {
            log.info("Got Standard Lines for Topic {} ", topicUUID);
            return standardLine;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Standard Line: %s, not found!", topicUUID);
        }
    }

    void createStandardLineDocument(CreateStandardLineDocumentDto request) {
        log.info("creating Standard Lines -expiry {} ", request.getExpires());
        UUID documentUUID = documentClient.createDocument(SL_EXT_REF_UUID, request.getDisplayName(), ManagedDocumentType.STANDARD_LINE);

        expireExistingStandardLineIfExist(request);

        saveNewStandardLine(request, documentUUID);

        processDocument(request,documentUUID);
        log.info("Created Standard Line - {}", request.getDisplayName());
    }

    public StandardLine getStandardLineList(UUID caseUUID) {
        GetCaseworkCaseDataResponse caseTypeResponse = caseworkClient.getCase(caseUUID);
        return getStandardLines(caseTypeResponse.getPrimaryTopic());
    }

    private void expireExistingStandardLineIfExist(CreateStandardLineDocumentDto request) {
        StandardLine standardLine = standardLineRepository.findStandardLinesByTopicAndExpires(request.getTopicUUID(), LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        if (standardLine != null) {
            standardLine.setExpires();
            standardLineRepository.save(standardLine);
            deleteDocument(SL_EXT_REF_UUID, standardLine.getUuid());
            log.info("Set Expiry to now for Standard Line - {}, id {}", standardLine.getDisplayName(), standardLine.getUuid());
        }
    }

    private void saveNewStandardLine(CreateStandardLineDocumentDto request, UUID documentUUID) {
        StandardLine newStandardLine = new StandardLine(documentUUID,request.getDisplayName(), request.getTopicUUID(), LocalDateTime.of(request.getExpires(), LocalTime.MAX));
        standardLineRepository.save(newStandardLine);
    }

    private void processDocument(CreateStandardLineDocumentDto document, UUID documentUUID){
        if (document != null) {

            documentClient.processDocument(ManagedDocumentType.STANDARD_LINE, documentUUID, document.getS3UntrustedUrl());
        }
    }

    private void deleteDocument(UUID externalReferenceUUID, UUID documentUUID) {
        documentClient.deleteDocument(externalReferenceUUID, documentUUID);
    }
}
