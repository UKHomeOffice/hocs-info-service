package uk.gov.digital.ho.hocs.info.entities;

import lombok.Getter;

public enum House {

    HOUSE_LORDS("House of Lords"),
    HOUSE_COMMONS("House of Commons"),
    HOUSE_SCOTTISH_PARLIAMENT("Scottish Parliament"),
    HOUSE_NORTHERN_IRISH_ASSEMBLY("Northern Irish Assembly"),
    HOUSE_EUROPEAN_PARLIAMENT("European Parliament"),
    HOUSE_WELSH_ASSEMBLY("Welsh Assembly");

    @Getter
    private String displayValue;

    House(String value) {
        displayValue = value;
    }
}