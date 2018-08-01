package uk.gov.digital.ho.hocs.info.exception;

public class EntityPermissionException extends Exception {

    public EntityPermissionException(String msg) {
        super(msg);
    }

    public EntityPermissionException(String msg, Object... args) {
        super(String.format(msg, args));
    }
}
