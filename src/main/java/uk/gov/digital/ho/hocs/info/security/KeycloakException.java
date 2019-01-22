package uk.gov.digital.ho.hocs.info.security;

public class KeycloakException extends RuntimeException {

    public KeycloakException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeycloakException(String message) {
        super(message);
    }
}
