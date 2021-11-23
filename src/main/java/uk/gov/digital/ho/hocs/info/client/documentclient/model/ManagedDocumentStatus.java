package uk.gov.digital.ho.hocs.info.client.documentclient.model;

import lombok.Getter;

public enum ManagedDocumentStatus {

    PENDING("Pending"),
    ACTIVE("Active"),
    EXPIRED("Expired");

    @Getter
    private String displayValue;

    ManagedDocumentStatus(String value) {
        displayValue = value;
    }
}
