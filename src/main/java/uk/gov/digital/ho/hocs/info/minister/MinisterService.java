package uk.gov.digital.ho.hocs.info.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.entities.Minister;
import uk.gov.digital.ho.hocs.info.entities.ParentTopic;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.member.MemberService;
import uk.gov.digital.ho.hocs.info.repositories.MinisterRepository;
import uk.gov.digital.ho.hocs.info.repositories.ParentTopicRepository;
import uk.gov.digital.ho.hocs.info.tenant.TenantService;

import java.util.ArrayList;
import java.util.List;

@Service
public class MinisterService {

    private final MinisterRepository ministerRepository;
    // TODO private final TenantService tenantService;

    @Autowired
    public MinisterService(MinisterRepository ministerRepository) {
        this.ministerRepository = ministerRepository;
    }

    public Minister getMinisterFromTopicId(Long id) {
        return ministerRepository.findByTopicId(id);
    }
}
