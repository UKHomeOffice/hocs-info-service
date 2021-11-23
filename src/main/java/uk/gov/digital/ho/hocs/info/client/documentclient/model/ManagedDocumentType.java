package uk.gov.digital.ho.hocs.info.client.documentclient.model;

import lombok.Getter;

public enum ManagedDocumentType {

    TEMPLATE("Template"),
    STANDARD_LINE("Standard Line");

    @Getter
    private String displayValue;

    ManagedDocumentType(String value) {
        displayValue = value;
    }
}
