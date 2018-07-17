package uk.gov.digital.ho.hocs.info;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class HocsInfoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HocsInfoServiceApplication.class, args);
    }


    public static boolean isNullOrEmpty(String value) {
        if (value == null) {
            return true;
        } else {
            return value.trim().equals("");
        }
    }
    public static boolean isNullOrEmpty(LocalDate value) {
        return value == null;
    }

}
