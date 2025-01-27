package com.beransantur.loanapi.service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;

@Getter
@Setter
@Builder
public class Installment {
    private Integer id;
    private Loan loan;
    private BigDecimal amount;
    private BigDecimal paidAmount;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    @Builder.Default
    private Boolean isPaid = Boolean.FALSE;

    public boolean isPaymentBeforeThreeMonthsThanDueDate(LocalDate paymentDate) {
        LocalDate dateBefore = paymentDate;
        LocalDate dateAfter = dueDate;

        long monthsBetween = MONTHS.between(
                dateBefore,
                dateAfter);

        return monthsBetween >= 3;
    }

    public boolean isPaymentBeforeDueDate(LocalDate paymentDate) {
        LocalDate dateBefore = dueDate;
        LocalDate dateAfter = paymentDate;

        long daysBetween = DAYS.between(
                dateBefore,
                dateAfter);

        return daysBetween < 0;
    }

    public boolean isPaymentAfterDueDate(LocalDate paymentDate) {
        LocalDate dateBefore = dueDate;
        LocalDate dateAfter = paymentDate;

        long daysBetween = DAYS.between(
                dateBefore,
                dateAfter);

        return daysBetween > 0;
    }

    public BigDecimal calculateDiscount(BigDecimal paidAmount, LocalDate paymentDate) {
        LocalDate dateBefore = paymentDate;
        LocalDate dateAfter = dueDate;

        long daysBetween = DAYS.between(
                dateBefore,
                dateAfter);

        final String DISCOUNT_COEFFICIENT = "0.001";
        BigDecimal discountAmount = amount.multiply(new BigDecimal(DISCOUNT_COEFFICIENT)).multiply(new BigDecimal(daysBetween));
        return paidAmount.subtract(discountAmount);
    }

    public BigDecimal calculatePenalty(BigDecimal amount, LocalDate paymentDate) {
        LocalDate dateBefore = dueDate;
        LocalDate dateAfter = paymentDate;

        long daysBetween = DAYS.between(
                dateBefore,
                dateAfter);

        final String PENALTY_COEFFICIENT = "0.001";
        BigDecimal penaltyAmount = amount.multiply(new BigDecimal(PENALTY_COEFFICIENT)).multiply(new BigDecimal(daysBetween));
        return paidAmount.subtract(penaltyAmount);
    }

}
