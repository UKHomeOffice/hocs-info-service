package uk.gov.digital.ho.hocs.info.bulkupdate;

public class BulkImportException extends RuntimeException {
    public BulkImportException(String message, Exception cause) {
        super(message, cause);
    }

    public BulkImportException(String message) {
        super(message);
    }
}
