package uk.gov.digital.ho.hocs.info.managedDocuments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ManagedDocumentService {

//    private final StandardLineRepository standardLineRepository;
//    private final TemplateRepository templateRepository;
//    private final StandardLineTopicRepository standardLineTopicRepository;
//    private final DocumentClient documentClient;
//    private final UUID STANDARD_LINE_EXTERNAL_REFERENCE_UUID = UUID.fromString("77777777-7777-7777-7777-777777777777");
//    private final UUID TEMPLATE_EXTERNAL_REFERENCE_UUID = UUID.fromString("88888888-8888-8888-8888-888888888888");
//
//    @Autowired
//    public ManagedDocumentService(TemplateRepository templateRepository,
//                                  StandardLineRepository standardLineRepository,
//                                  StandardLineTopicRepository standardLineTopicRepository,
//                                  DocumentClient documentClient) {
//        this.standardLineRepository = standardLineRepository;
//        this.templateRepository = templateRepository;
//        this.standardLineTopicRepository = standardLineTopicRepository;
//        this.documentClient = documentClient;
//    }
//
//    void createDocument(CreateStandardLineDocumentDto request) {
//        if (request.getType().equals(ManagedDocumentType.STANDARD_LINE)) {
//            createStandardLineDocument(request);
//        }
//        if (request.getType().equals(ManagedDocumentType.TEMPLATE)) {
//                createDocument(TEMPLATE_EXTERNAL_REFERENCE_UUID, request);
//        }
//
//
//    }
//
//    private void createStandardLineDocument(CreateStandardLineDocumentDto request) {
//        log.info("Creating Standard Line - {}", request.getDisplayName());
//        StandardLine standardLine = standardLineRepository.findByDisplayNameAndTopic(request.getDisplayName(), request.getTopicUUID());
//        if (standardLine != null) {
//            StandardLineTopic standardLineTopic = standardLineTopicRepository.findByStandardLineUuid(standardLine.getUuid());
//            standardLineTopic.delete();
//            standardLineTopicRepository.save(standardLineTopic);
//            try {
//                deleteDocument(STANDARD_LINE_EXTERNAL_REFERENCE_UUID,standardLine.getUuid());
//            } catch (EntityCreationException e) {
//                e.printStackTrace();
//            }
//            log.info("Set Deleted to True for Standard Line - {}, id {}", standardLine.getDisplayName(), standardLine.getUuid());
//        }
//
//        StandardLine newStandardLine = new StandardLine(request.getDisplayName(), request.getExpires());
//        standardLineRepository.save(newStandardLine);
//
//        StandardLineTopic standardLineTopic = new StandardLineTopic(newStandardLine.getUuid(), "DCU", request.getTopicUUID());
//        standardLineTopicRepository.save(standardLineTopic);
//        log.info("created Standard line for {} with id {}", newStandardLine.getDisplayName(), newStandardLine.getUuid());
//
//        try {
//            createDocument(STANDARD_LINE_EXTERNAL_REFERENCE_UUID, request);
//        } catch (EntityCreationException e) {
//            e.printStackTrace();
//        }
//    }
//
//    void createDocument(UUID externalReferenceUUID, CreateStandardLineDocumentDto document) throws EntityCreationException {
//        if (document != null) {
//            UUID response = documentClient.createDocument(externalReferenceUUID, document.getDisplayName(), document.getType());
//
//            documentClient.processDocument(document.getType(), response, document.getS3UntrustedUrl());
//        }
//    }
//
//    void deleteDocument(UUID externalReferenceUUID, UUID documentUUID) throws EntityCreationException {
//        documentClient.deleteDocument(externalReferenceUUID, documentUUID);
//
//    }
}
