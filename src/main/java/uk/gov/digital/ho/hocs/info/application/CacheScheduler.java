package uk.gov.digital.ho.hocs.info.application;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import uk.gov.digital.ho.hocs.info.api.TeamService;
import uk.gov.digital.ho.hocs.info.api.UserService;

@Configuration
@EnableScheduling
@AllArgsConstructor
@Profile("!test")
public class CacheScheduler {

    private UserService userService;

    @Scheduled(fixedDelayString = "${cache.user.refresh}000")
    public void refreshUserCache(){
        userService.refreshUserCache();
    }
}
