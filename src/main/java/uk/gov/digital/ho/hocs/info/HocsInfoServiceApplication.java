package uk.gov.digital.ho.hocs.info;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

import javax.annotation.PreDestroy;

@Slf4j
@SpringBootApplication
@EnableRetry
public class HocsInfoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HocsInfoServiceApplication.class, args);
    }

    @PreDestroy
    public void stop() {
        log.info("hocs-info-service stopping gracefully");
    }

}
