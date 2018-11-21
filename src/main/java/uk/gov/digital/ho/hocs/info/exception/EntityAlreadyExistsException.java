package uk.gov.digital.ho.hocs.info.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String e) {
        super(e);
    }
}
