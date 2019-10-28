package uk.gov.digital.ho.hocs.info;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class AppContextEventListener {

    @EventListener
    public void handleContextRefreshed(ContextRefreshedEvent event) {
        log.info("************************* APPLICATION PROPERTIES ******************************");
        printActiveProperties((ConfigurableEnvironment) event.getApplicationContext().getEnvironment());
        log.info("******************************************************************************");
    }

    private void printActiveProperties(ConfigurableEnvironment env) {

        List<MapPropertySource> propertySources = new ArrayList<>();

        env.getPropertySources().forEach(it -> {
            if (it instanceof MapPropertySource && it.getName().contains("applicationConfig")) {
                propertySources.add((MapPropertySource) it);
            }
        });
        propertySources.stream()
                       .map(propertySource -> propertySource.getSource().keySet())
                       .flatMap(Collection ::stream)
                       .distinct()
                       .sorted()
                       .forEach(key -> {
                           try {
                               log.info(key + "=" + env.getProperty(key));
                           } catch (Exception e) {
                               log.warn("{} -> {}", key, e.getMessage());
                           }
                       });
    }
}
