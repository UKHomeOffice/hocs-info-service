package uk.gov.digital.ho.hocs.info.security;

public class BulkImportException extends RuntimeException {
    public BulkImportException(String message, Exception cause) {
        super(message, cause);
    }
}
