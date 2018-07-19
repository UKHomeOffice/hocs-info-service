package uk.gov.digital.ho.hocs.info.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.entities.ParentTopic;
import uk.gov.digital.ho.hocs.info.entities.Tenant;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.ParentTopicRepository;
import uk.gov.digital.ho.hocs.info.repositories.TenantRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicService {


    private final ParentTopicRepository parentTopicRepository;
    private final TenantRepository tenantRepository;


    @Autowired
    public TopicService(ParentTopicRepository parentTopicRepository, TenantRepository tenantRepository) {
        this.parentTopicRepository = parentTopicRepository;
        this.tenantRepository = tenantRepository;
    }

    public List<ParentTopic> getTopics(List<String> roles) {
        if (roles != null) {
            List<String> tenants = getTenantsFromRoles(roles);

            List<ParentTopic> parentTopics = new ArrayList<>();
            for (String tenant : tenants) {
                parentTopics = parentTopicRepository.findParentTopicByTenant(tenant);
            }
            return parentTopics;
        } else {
            throw new EntityNotFoundException("Roles is Null!");
        }
    }

    private List<String> getTenantsFromRoles(List<String> roles) {
        List<Tenant> allTenants = (List<Tenant>) tenantRepository.findAll();
        List<String> tenants = new ArrayList<>();
        roles.forEach(role ->
                allTenants.stream().filter(x -> role.equals(x.getDisplayName())).forEach(x ->
                        tenants.add(role)

                ));
        return tenants;
    }
}
