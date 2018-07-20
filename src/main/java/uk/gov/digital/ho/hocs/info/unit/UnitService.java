package uk.gov.digital.ho.hocs.info.unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.entities.Unit;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.UnitRepository;
import uk.gov.digital.ho.hocs.info.tenant.TenantService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UnitService {

    private final UnitRepository unitRepository;

    private final TenantService tenantService;

    @Autowired
    public UnitService(UnitRepository unitRepository, TenantService tenantService) {
        this.unitRepository = unitRepository;
        this.tenantService = tenantService;
    }


    public List<Unit> getUnits(List<String> roles) {
        if (roles != null) {
            List<String> tenants = tenantService.getTenantsFromRoles(roles);

            List<Unit> parentTopics = new ArrayList<>();
            for (String tenant : tenants) {
                parentTopics = unitRepository.findUnitByTenant(tenant);
            }
            return parentTopics;
        } else {
            throw new EntityNotFoundException("Roles is Null!");
        }
    }}

