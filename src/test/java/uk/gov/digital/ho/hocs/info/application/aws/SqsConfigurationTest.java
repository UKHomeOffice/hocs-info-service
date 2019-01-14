package uk.gov.digital.ho.hocs.info.application.aws;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class SqsConfigurationTest {


    private uk.gov.digital.ho.hocs.info.aws.SqsConfiguration config;

    @Before
    public void setup() {
        config = new uk.gov.digital.ho.hocs.info.aws.SqsConfiguration();
    }

    @Test
    public void shouldThrowExceptionWhenNullAccessKey() {
        assertThatThrownBy(() -> config.docsSqsClient(null, "some secret key", "some region")).
                isInstanceOf(BeanCreationException.class);
    }

    @Test
    public void shouldThrowExceptionWhenNullSecretKey() {
        assertThatThrownBy(() -> config.docsSqsClient("some access key", null, "some region")).
                isInstanceOf(BeanCreationException.class);
    }

    @Test
    public void shouldThrowExceptionWhenNullRegion() {
        assertThatThrownBy(() -> config.docsSqsClient("some access key", "some secret key", null)).
                isInstanceOf(BeanCreationException.class);
    }


}