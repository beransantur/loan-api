package com.beransantur.loanapi.service.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class Customer {
    private String name;
    private String surname;
    private BigDecimal creditLimit;
    private BigDecimal usedCreditLimit;
    private List<Loan> loans;

    public boolean hasEnoughLimit(BigDecimal amount) {
        if (creditLimit.compareTo(amount) < 0) {
            return false;
        }

        return true;
    }

    public void reduceLimit(BigDecimal amount) {
        usedCreditLimit = usedCreditLimit.add(amount);
        creditLimit = creditLimit.subtract(amount);
    }
}
