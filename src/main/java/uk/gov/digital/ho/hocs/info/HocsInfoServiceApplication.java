package uk.gov.digital.ho.hocs.info;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Environment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;
import java.util.Properties;

@Slf4j
@SpringBootApplication
public class HocsInfoServiceApplication {

    public static void main(String[] args) {

        log.debug("System Properties:");
        log.debug("==============================");
        Properties properties = System.getProperties();
        for (Map.Entry entry : properties.entrySet()) {
            log.debug(entry.getKey() + " : " + entry.getValue());
        }
        log.debug("Environment Properties:");
        log.debug("==============================");
        properties = Environment.getProperties();
        for (Map.Entry entry : properties.entrySet()) {
            log.debug(entry.getKey() + " : " + entry.getValue());
        }

        SpringApplication.run(HocsInfoServiceApplication.class, args);
    }
}
