package uk.gov.digital.ho.hocs.info;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class HocsInfoServiceApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(HocsInfoServiceApplication.class, args);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
