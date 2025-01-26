package com.beransantur.loanapi.service.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class Loan {
    private Integer id;
    private BigDecimal totalAmount;
    private Integer installmentNumber;
    private Customer customer;
    @Builder.Default
    private Boolean isPaid = Boolean.FALSE;
    private List<Installment> installments;
    @Builder.Default
    private LocalDate createdAt = LocalDate.now();

    public void setTotalAmount(BigDecimal initialAmount, BigDecimal interestRate) {
        this.totalAmount = calculateTotalAmount(initialAmount, interestRate);
    }

    public void addInstallments() {
        BigDecimal installmentAmount = calculateInstallmentAmount();
        LocalDate firstOfNextMonth = getFirstDayOfNextMonth(createdAt);
        List<Installment> installments = new ArrayList<>(installmentNumber);

        for (int i = 0; i < installmentNumber; i++) {
            Installment installment = Installment.builder()
                    .loan(this)
                    .amount(installmentAmount)
                    .dueDate(firstOfNextMonth)
                    .build();

            firstOfNextMonth = getFirstDayOfNextMonth(firstOfNextMonth);
            installments.add(installment);
        }

        this.installments = installments;
    }

    private BigDecimal calculateTotalAmount(BigDecimal initialAmount, BigDecimal interestRate) {
        return initialAmount.multiply(interestRate.add(new BigDecimal(1)));
    }

    private BigDecimal calculateInstallmentAmount() {
        return totalAmount.divide(new BigDecimal(installmentNumber), 2, RoundingMode.HALF_UP);
    }

    private LocalDate getFirstDayOfNextMonth(LocalDate time) {
        return time
                .withMonth(createdAt.getMonthValue() + 1)
                .withDayOfMonth(1);
    }
}
