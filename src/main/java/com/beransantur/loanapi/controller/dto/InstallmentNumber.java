package com.beransantur.loanapi.controller.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum InstallmentNumber {
    SIX_MONTHS(6),
    NINE_MONTHS(9),
    TWELVE_MONTHS(12),
    TWENTYFOUR_MONTHS(24);

    private final int value;

    InstallmentNumber(final int newValue) {
        value = newValue;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
