package com.beransantur.loanapi.service.model;

public enum InstallmentNumber {
    THREE_MONTHS(3),
    SIX_MONTHS(6),
    NINE_MONTHS(9),
    TWELVE_MONTHS(12),
    TWENTYFOUR_MONTHS(24);

    private final int value;

    InstallmentNumber(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
