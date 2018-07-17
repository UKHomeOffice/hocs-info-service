package uk.gov.digital.ho.hocs.info.caseType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.dto.CaseTypeDto;
import uk.gov.digital.ho.hocs.info.entities.CaseType;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.CaseTypeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CaseTypeService {

    private final CaseTypeRepository caseTypeRepository;

    @Autowired
    public CaseTypeService(CaseTypeRepository caseTypeRepository) {
        this.caseTypeRepository = caseTypeRepository;
    }

    public List<CaseTypeDto> getCaseTypes(List<String> tenants) {
        if (tenants != null) {
            List<CaseTypeDto> caseTypeDtos = new ArrayList<>();
            tenants.forEach(tenant -> {
                List<CaseType> caseTypes = caseTypeRepository.findCaseTypesByTenant(tenant);
                if (caseTypes != null) {
                    caseTypes.stream().map(caseType -> new CaseTypeDto(tenant, caseType.getDisplayName(), caseType.getType())).forEach(caseTypeDtos::add);
                } else {
                    throw new EntityNotFoundException("Tenant not Found!");
                }
            });


            return caseTypeDtos;
        } else {
            throw new EntityNotFoundException("Tenant not Found!");
        }
    }
}
