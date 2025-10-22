package com.cs203.core.enums;

import lombok.Getter;

@Getter
public enum TariffType {
    SPECIFIC("specific"),
    AD_VALOREM("ad_valorem");

    private final String value;

    TariffType(String value) {
        this.value = value;
    }

    public static TariffType fromValue(String value) {
        for (TariffType type : values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown tariff type: " + value);
    }
}
