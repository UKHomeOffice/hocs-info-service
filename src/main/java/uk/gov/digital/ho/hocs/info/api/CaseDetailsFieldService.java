package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.CaseDetailsFieldDto;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseDetailsFieldRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CaseDetailsFieldService {

    private final CaseDetailsFieldRepository caseDetailsFieldRepository;

    @Autowired
    public CaseDetailsFieldService(CaseDetailsFieldRepository caseDetailsFieldRepository) {
        this.caseDetailsFieldRepository = caseDetailsFieldRepository;
    }


    public List<CaseDetailsFieldDto> getCaseDetailsFieldsByCaseType(String caseType) {
        return caseDetailsFieldRepository.findByCaseTypeOrderById(caseType).stream().map(CaseDetailsFieldDto::from).collect(Collectors.toList());
    }


}
