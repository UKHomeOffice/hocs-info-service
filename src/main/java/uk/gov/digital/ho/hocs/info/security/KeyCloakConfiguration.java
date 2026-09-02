package uk.gov.digital.ho.hocs.info.security;

import jakarta.ws.rs.client.ClientBuilder;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class KeyCloakConfiguration {

    @Bean
    public Keycloak keycloakClient(@Value("${keycloak.server.url}") String serverUrl,
                                   @Value("${keycloak.realm}") String realm,
                                   @Value("${keycloak.username}") String username,
                                   @Value("${keycloak.password}") String password,
                                   @Value("${keycloak.client.id}") String clientId,
                                   @Value("${keycloak.http.connect-timeout-ms}") int connectTimeoutMs,
                                   @Value("${keycloak.http.socket-timeout-ms}") int socketTimeoutMs,
                                   @Value("${keycloak.http.connection-request-timeout-ms}") int connectionRequestTimeoutMs,
                                   @Value("${keycloak.http.max-retries}") int maxRetries) {

        if (!StringUtils.hasText(serverUrl)) {
            throw new BeanCreationException("Failed to create Keycloak client bean. Need non-blank value for serverUrl");
        }
        if (!StringUtils.hasText(realm)) {
            throw new BeanCreationException("Failed to create Keycloak client bean. Need non-blank value for realm");
        }
        if (!StringUtils.hasText(username)) {
            throw new BeanCreationException("Failed to create Keycloak client bean. Need non-blank value for username");
        }
        if (!StringUtils.hasText(password)) {
            throw new BeanCreationException("Failed to create Keycloak client bean. Need non-blank value for password");
        }
        if (!StringUtils.hasText(clientId)) {
            throw new BeanCreationException("Failed to create Keycloak client bean. Need non-blank value for clientId");
        }

        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(connectTimeoutMs)
            .setSocketTimeout(socketTimeoutMs)
            .setConnectionRequestTimeout(connectionRequestTimeoutMs)
            .build();

        ApacheHttpClient43Engine engine = new ApacheHttpClient43Engine(
            HttpClientBuilder.create()
                .useSystemProperties()
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(maxRetries, false))
                .build()
        );

        return KeycloakBuilder.builder()
                              .serverUrl(serverUrl)
                              .realm(realm)
                              .username(username)
                              .password(password)
                              .clientId(clientId)
                              .resteasyClient(
                                  ((ResteasyClientBuilder) ClientBuilder.newBuilder())
                                      .httpEngine(engine)
                                      .build()
                              )
                              .build();
    }

}
