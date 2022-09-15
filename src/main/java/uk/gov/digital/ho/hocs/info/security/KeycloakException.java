package uk.gov.digital.ho.hocs.info.security;

public class KeycloakException extends RuntimeException {

    private Integer httpStatus;

    public KeycloakException(String message, Integer httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public KeycloakException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeycloakException(String message) {
        super(message);
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

}
