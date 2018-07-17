package uk.gov.digital.ho.hocs.info.caseType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.dto.CaseTypeDto;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeDetail;
import uk.gov.digital.ho.hocs.info.entities.Tenant;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.CaseTypeRepository;
import uk.gov.digital.ho.hocs.info.repositories.TenantRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CaseTypeService {

    private final CaseTypeRepository caseTypeRepository;
    private final TenantRepository tenantRepository;

    @Autowired
    public CaseTypeService(CaseTypeRepository caseTypeRepository, TenantRepository tenantRepository) {
        this.caseTypeRepository = caseTypeRepository;
        this.tenantRepository = tenantRepository;
    }

    public List<CaseTypeDto> getCaseTypes(List<String> rolls) {
        if (rolls != null) {
            List<String> tenants = getTenantsFromRolls(rolls);

            List<CaseTypeDto> caseTypeDtos = new ArrayList<>();
            tenants.forEach(tenant -> {
                List<CaseTypeDetail> caseTypeDetails = caseTypeRepository.findCaseTypesByTenant(tenant);
                if (caseTypeDetails != null) {
                    caseTypeDetails.stream().map(caseTypeDetail -> new CaseTypeDto(tenant, caseTypeDetail.getDisplayName(), caseTypeDetail.getType())).forEach(caseTypeDtos::add);
                } else {
                    throw new EntityNotFoundException("Tenant not Found!");
                }
            });
            return caseTypeDtos;
        } else {
            throw new EntityNotFoundException("Rolls is Null!");
        }
    }

    private List<String> getTenantsFromRolls(List<String> rolls) {
        List<Tenant> allTenants = (List<Tenant>) tenantRepository.findAll();
        List<String> tenants = new ArrayList<>();
        rolls.forEach(roll -> {
            allTenants.stream().filter(x -> roll.equals(x.getDisplayName())).forEach(x -> {
                tenants.add(roll);
            });
        });
        return tenants;
    }
}
