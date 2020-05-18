package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalDateWrapperImplTest {

    private LocalDateWrapperImpl localDateWrapper;

    @Before
    public void before(){
        localDateWrapper = new LocalDateWrapperImpl();
    }

    @Test
    public void now(){
        assertThat(localDateWrapper.now()).isEqualTo(LocalDate.now());
    }
}
