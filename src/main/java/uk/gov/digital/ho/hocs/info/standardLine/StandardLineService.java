package uk.gov.digital.ho.hocs.info.standardLine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.entities.StandardLine;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.repositories.StandardLineRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class StandardLineService {

    private final StandardLineRepository standardLineRepository;

    @Autowired
    public StandardLineService(StandardLineRepository standardLineRepository) {
        this.standardLineRepository = standardLineRepository;
    }

    public List<StandardLine> getStandardLines(UUID topicUUID) throws EntityPermissionException {
        log.info("Requesting Standard Lines for Case type {} and Topic {} ", topicUUID);
            return standardLineRepository.findStandardLinesByTopic(topicUUID);
    }
}
