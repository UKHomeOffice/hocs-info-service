package uk.gov.digital.ho.hocs.info.api.Minister;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.api.dto.GetMinistersResponse;
import uk.gov.digital.ho.hocs.info.domain.model.Minister;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
public class MinisterResource {

    private final MinisterService ministerService;

    @Autowired
    public MinisterResource(MinisterService ministerService) {
        this.ministerService = ministerService;
    }

    @GetMapping(value = "/ministers", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetMinistersResponse> getAllMinisters() {
        Set<Minister> ministers = ministerService.getMinisters();
        return ResponseEntity.ok(GetMinistersResponse.from(ministers));
    }
}