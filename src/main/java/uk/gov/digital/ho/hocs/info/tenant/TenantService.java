package uk.gov.digital.ho.hocs.info.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.entities.Tenant;
import uk.gov.digital.ho.hocs.info.repositories.TenantRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;

    @Autowired
    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public List<String> getTenantsFromRoles(List<String> roles) {
        List<Tenant> allTenants = (List<Tenant>) tenantRepository.findAll();
        List<String> tenants = new ArrayList<>();
        roles.forEach(role ->
                allTenants.stream().filter(x -> role.equals(x.getDisplayName())).forEach(x ->
                        tenants.add(role)
                ));
        return tenants;
    }
}
