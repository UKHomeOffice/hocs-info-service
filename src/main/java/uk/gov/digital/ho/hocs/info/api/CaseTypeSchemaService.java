package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseTypeRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseTypeSchemaRepository;

import java.util.List;

@Service
@Slf4j
public class CaseTypeSchemaService {

    private final CaseTypeSchemaRepository caseTypeSchemaRepository;
    private final CaseTypeRepository caseTypeRepository;

    @Autowired
    public CaseTypeSchemaService(CaseTypeSchemaRepository caseTypeSchemaRepository,
                                 CaseTypeRepository caseTypeRepository) {
        this.caseTypeSchemaRepository = caseTypeSchemaRepository;
        this.caseTypeRepository = caseTypeRepository;
    }

    List<String> getCaseTypeStages(String type) {
        log.debug("Getting CaseType stages for type {}", type);

        if (caseTypeRepository.findByType(type) == null) {
            String errorMessage = String.format("CaseType %s does not exist", type);
            log.error(errorMessage);
            throw new ApplicationExceptions.EntityNotFoundException(errorMessage);
        }

        List<String> caseTypeDistinctStages = caseTypeSchemaRepository.findDistinctStagesByCaseType(type);
        log.info("Got {} CaseTypes Stages for CaseType {}", caseTypeDistinctStages.size(), type);
        return caseTypeDistinctStages;
    }

}
