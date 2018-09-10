package uk.gov.digital.ho.hocs.info.house;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.exception.IngestException;

@RestController
@Slf4j
public class HouseResource {
    private final HouseService houseService;

    @Autowired
    public HouseResource(HouseService houseService) {
        this.houseService = houseService;
    }

    @GetMapping(value = "/houses/refresh")
    public ResponseEntity getFromApi() {
        log.info("Updating Houses");
        try {
            houseService.updateWebMemberLists();
            return ResponseEntity.ok().build();
        } catch (IngestException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}