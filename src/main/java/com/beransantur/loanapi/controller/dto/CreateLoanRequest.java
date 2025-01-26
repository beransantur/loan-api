package com.beransantur.loanapi.controller.dto;

import com.beransantur.loanapi.service.model.InstallmentNumber;
import com.beransantur.loanapi.service.model.Loan;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLoanRequest {
    @NotNull
    @PositiveOrZero
    private BigDecimal amount;

    @DecimalMin(value = "0.1")
    @DecimalMax(value = "0.5")
    private BigDecimal interestRate;

    private InstallmentNumber installmentNumber;

    public Loan toModel() {
        Loan loan = Loan.builder()
                .installmentNumber(installmentNumber.getValue())
                .build();
        loan.setTotalAmount(amount, interestRate);
        return loan;
    }

}
