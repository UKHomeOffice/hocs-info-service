package uk.gov.digital.ho.hocs.info;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@Slf4j
@SpringBootApplication
public class HocsInfoServiceApplication {

    public static void main(String[] args) {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader("./application.properties"));
            log.debug("Application Properties:");
            log.debug("==============================");
            for (Map.Entry entry :  properties.entrySet()) {
                log.debug(entry.getKey() + " : " + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SpringApplication.run(HocsInfoServiceApplication.class, args);
    }
}
