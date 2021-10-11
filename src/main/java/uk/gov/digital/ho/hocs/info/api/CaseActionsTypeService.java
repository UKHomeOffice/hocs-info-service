package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.CaseActionTypeDto;
import uk.gov.digital.ho.hocs.info.domain.model.CaseActionType;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseActionTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CaseActionsTypeService {

    private final CaseActionTypeRepository caseActionTypeRepository;

    @Autowired
    public CaseActionsTypeService(CaseActionTypeRepository caseActionTypeRepository) {
        this.caseActionTypeRepository = caseActionTypeRepository;
    }

    public List<CaseActionTypeDto> getCaseActionsByCaseType(String caseType) {

        List<CaseActionType> caseActionEntities = caseActionTypeRepository.findAllByCaseTypeAndActiveIsTrue(caseType);

        return caseActionEntities.stream().map(CaseActionTypeDto::from).collect(Collectors.toList());
    }
}
