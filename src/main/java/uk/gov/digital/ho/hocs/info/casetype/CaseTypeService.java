package uk.gov.digital.ho.hocs.info.casetype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.dto.CaseTypeDto;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeDetail;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.CaseTypeRepository;
import uk.gov.digital.ho.hocs.info.tenant.TenantService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CaseTypeService {

    private final CaseTypeRepository caseTypeRepository;

    private final TenantService tenantService;

    @Autowired
    public CaseTypeService(final CaseTypeRepository caseTypeRepository, final TenantService tenantService) {
        this.caseTypeRepository = caseTypeRepository;
        this.tenantService = tenantService;
    }

    public List<CaseTypeDto> getCaseTypes(final List<String> roles) {
        if (roles != null) {
            List<String> tenants = tenantService.getTenantsFromRoles(roles);

            List<CaseTypeDto> caseTypeDtos = new ArrayList<>();
            for (String tenant : tenants) {
                List<CaseTypeDetail> caseTypeDetails =
                        caseTypeRepository.findCaseTypesByTenant(tenant);
                if (caseTypeDetails != null) {
                    caseTypeDetails.stream().map(caseTypeDetail ->
                            new CaseTypeDto(tenant,
                                    caseTypeDetail.getDisplayName(),
                                    caseTypeDetail.getType())).forEach(caseTypeDtos::add);
                } else {
                    throw new EntityNotFoundException("Tenant not Found!");
                }
            }
            return caseTypeDtos;
        } else {
            throw new EntityNotFoundException("Roles is Null!");
        }
    }
}
