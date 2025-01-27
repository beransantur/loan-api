package com.beransantur.loanapi.controller.dto;

import com.beransantur.loanapi.service.model.Loan;
import jakarta.validation.constraints.*;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class CreateLoanRequest {
    @NotNull
    @PositiveOrZero
    BigDecimal amount;

    @NotNull
    @DecimalMin(value = "0.1")
    @DecimalMax(value = "0.5")
    BigDecimal interestRate;

    @NotNull
    InstallmentNumber installmentNumber;

    public Loan toModel() {
        Loan loan = Loan.builder()
                .installmentNumber(installmentNumber.getValue())
                .build();
        return loan;
    }

}
