package uk.gov.digital.ho.hocs.info.domain.model.enums;

import lombok.Getter;

public enum HouseCodes {

    IRISH_ASSEMBLY("NI"),
    HOUSE_OF_COMMONS("HC"),
    HOUSE_OF_LORDS("HL"),
    SCOTTISH_PARLIAMENT("SP"),
    WELSH_ASSEMBLY("WA");

    @Getter
    private final String houseCode;

    HouseCodes(final String houseCode) {
        this.houseCode = houseCode;
    }
}
