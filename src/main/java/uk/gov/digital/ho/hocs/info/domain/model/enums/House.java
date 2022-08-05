package uk.gov.digital.ho.hocs.info.domain.model.enums;

import lombok.Getter;

public enum House {

    HOUSE_LORDS("House of Lords"),
    HOUSE_COMMONS("House of Commons"),
    HOUSE_SCOTTISH_PARLIAMENT("Scottish Parliament"),
    HOUSE_NORTHERN_IRISH_ASSEMBLY("Northern Ireland Assembly"),
    HOUSE_EUROPEAN_PARLIAMENT("European Parliament"),
    HOUSE_WELSH_ASSEMBLY("Welsh Parliament");

    @Getter
    private final String displayValue;

    House(final String value) {
        displayValue = value;
    }
}
