package uk.gov.digital.ho.hocs.info.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.entities.ParentTopic;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.ParentTopicRepository;
import uk.gov.digital.ho.hocs.info.tenant.TenantService;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicService {


    private final ParentTopicRepository parentTopicRepository;

    private final TenantService tenantService;

    @Autowired
    public TopicService(ParentTopicRepository parentTopicRepository, TenantService tenantService) {
        this.parentTopicRepository = parentTopicRepository;
        this.tenantService = tenantService;
    }

    public List<ParentTopic> getTopics(List<String> roles) {
        if (roles != null) {
            List<String> tenants = tenantService.getTenantsFromRoles(roles);

            List<ParentTopic> parentTopics = new ArrayList<>();
            for (String tenant : tenants) {
                parentTopics = parentTopicRepository.findParentTopicByTenant(tenant);
            }
            return parentTopics;
        } else {
            throw new EntityNotFoundException("Roles is Null!");
        }
    }
}
