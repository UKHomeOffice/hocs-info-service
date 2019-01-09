package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.*;
import uk.gov.digital.ho.hocs.info.domain.repository.FormRepository;

import java.util.Set;
import java.util.stream.Stream;

@Service
@Slf4j
public class FormService {

    private final FormRepository formRepository;

    @Autowired
    public FormService(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    Form getFormByType(String type) {
        log.debug("Getting Form for type {}", type);
        Form form = formRepository.findByType(type);
        if (form != null) {
            log.info("Got Form {} for type {}", form.getUuid(), type);
            return form;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Form for type %s was not found", type);
        }
    }

    Set<Form> getAllFormsForCaseType(String caseType) {
        log.debug("Getting all Forms for CaseType {}", caseType);
        Set<Form> caseTypeForms = formRepository.findAllActiveFormsByCaseType(caseType);
        log.info("Got {} Forms for CaseType {}", caseTypeForms.size(), caseType);
        return caseTypeForms;
    }

    Stream<Field> getAllFormsForCaseTypeSummary(String caseType) {
        Set<Form> caseTypeForms = getAllFormsForCaseType(caseType);
        log.debug("Filtering to summary only.");
        return caseTypeForms.stream().flatMap(f -> f.getFields().stream()).filter(Field::isSummary);
    }
}
