package uk.gov.digital.ho.hocs.info;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.TeamService;
import uk.gov.digital.ho.hocs.info.application.CacheScheduler;
import uk.gov.digital.ho.hocs.info.api.UserService;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CacheSchedulerTest {

    private CacheScheduler service;

    @Mock
    UserService userService;

    @Mock
    TeamService teamService;

    @Test
    public void shouldCallUserServiceRefresh() {
        service = new CacheScheduler(userService, teamService);
        service.refreshUserCache();
        verify(userService, times(1)).refreshUserCache();
    }

    @Test
    public void shouldCallTeamServiceRefresh() {
        service = new CacheScheduler(userService, teamService);
        service.refreshTeamCache();
        verify(teamService, times(1)).refreshTeamCache();
    }
}