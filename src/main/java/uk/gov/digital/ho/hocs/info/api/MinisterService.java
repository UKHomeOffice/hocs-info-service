package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.model.Minister;
import uk.gov.digital.ho.hocs.info.domain.repository.MinisterRepository;

import java.util.Set;

@Service
@Slf4j
public class MinisterService {

    private final MinisterRepository ministerRepository;

    @Autowired
    public MinisterService(MinisterRepository ministerRepository) {
        this.ministerRepository = ministerRepository;
    }

    public Set<Minister> getMinisters() {
        log.debug("Requesting all ministers");
        Set<Minister> ministers = ministerRepository.findAll();
        log.info("Got {} ministers", ministers.size());
        return ministers;
    }
}
