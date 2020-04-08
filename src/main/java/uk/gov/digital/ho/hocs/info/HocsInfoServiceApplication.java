package uk.gov.digital.ho.hocs.info;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class HocsInfoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HocsInfoServiceApplication.class, args);
    }
}
