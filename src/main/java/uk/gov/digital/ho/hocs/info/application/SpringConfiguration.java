package uk.gov.digital.ho.hocs.info.application;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;

@Configuration
public class SpringConfiguration implements WebMvcConfigurer {

    @Bean
    public RequestData createRequestData() {
        return new RequestData();
    }

    @Bean
    public RestTemplate createRestTemplate() {
        ClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(createHttpClient());
        return new RestTemplate(clientHttpRequestFactory);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(createRequestData());
    }

    @Bean
    public HttpClient createHttpClient() {
        return HttpClientBuilder.create()
                .useSystemProperties().build();
    }

    @Bean
    public ObjectMapper initialiseObjectMapper() {
        ObjectMapper m = new ObjectMapper();
        m.registerModule(new JavaTimeModule().addDeserializer(LocalDate.class, new MultiFormatLocalDateSerializer()));
        m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        m.enable(SerializationFeature.INDENT_OUTPUT);
        m.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        m.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return m;
    }

    @Bean(name = "json-jackson")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JacksonDataFormat jacksonDataFormat(ObjectMapper objectMapper) {
        return new JacksonDataFormat(objectMapper, Object.class);
    }


}
