package com.beransantur.loanapi.service.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Builder(toBuilder = true)
@Getter
public class Loan {
    private Integer id;
    private BigDecimal amount;
    private Integer installmentNumber;
    private Customer customer;
    @Builder.Default
    private Boolean isPaid = Boolean.FALSE;
    private List<Installment> installments;
    @Builder.Default
    private LocalDate createdAt = LocalDate.now();

    public void setTotalAmountWithInterest(BigDecimal initialAmount, BigDecimal interestRate) {
        this.amount = calculateTotalAmount(initialAmount, interestRate);
    }

    public void setInstallments() {
        BigDecimal installmentAmount = calculateInstallmentAmount();
        LocalDate firstDayOfNextMonth = getFirstDayOfNextMonth(createdAt);
        List<Installment> installments = new ArrayList<>(installmentNumber);

        for (int i = 0; i < installmentNumber; i++) {
            Installment installment = Installment.builder()
                    .loan(this)
                    .amount(installmentAmount)
                    .dueDate(firstDayOfNextMonth)
                    .build();

            installments.add(installment);
            firstDayOfNextMonth = getFirstDayOfNextMonth(firstDayOfNextMonth);
        }

        this.installments = installments;
    }

    public List<Installment> getInstallments() {
        return installments.stream()
                .sorted(Comparator.comparing(Installment::getDueDate))
                .collect(Collectors.toList());
    }

    public boolean isAllInstallmentsPaid() {
        return installments.stream().allMatch((installment -> installment.getIsPaid().equals(Boolean.TRUE)));
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    private BigDecimal calculateTotalAmount(BigDecimal initialAmount, BigDecimal interestRate) {
        return initialAmount.multiply(interestRate.add(new BigDecimal(1)));
    }

    private BigDecimal calculateInstallmentAmount() {
        return amount.divide(new BigDecimal(installmentNumber), 2, RoundingMode.HALF_UP);
    }

    private LocalDate getFirstDayOfNextMonth(LocalDate time) {
        if(time.getMonthValue() == 12) { //next year
            return time.withYear(time.getYear() + 1)
                    .withMonth(1)
                    .withDayOfMonth(1);
        }
        return time
                .withMonth(time.getMonthValue() + 1)
                .withDayOfMonth(1);
    }
}
