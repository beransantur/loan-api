package com.beransantur.loanapi.service.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InstallmentTest {

    @Test
    void shouldReturnTrue_WhenPaymentIsAtLeastThreeMonthsBeforeDueDate() {
        // given
        Installment installment = Installment.builder()
                .dueDate(LocalDate.of(2025, 6, 1))
                .build();
        LocalDate paymentDate = LocalDate.of(2025, 3, 1);

        // when
        boolean result = installment.isPaymentBeforeThreeMonthsThanDueDate(paymentDate);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalse_WhenPaymentIsLessThanThreeMonthsBeforeDueDate() {
        // Given
        Installment installment = Installment.builder()
                .dueDate(LocalDate.of(2025, 6, 1))
                .build();
        LocalDate paymentDate = LocalDate.of(2025, 4, 1);

        // When
        boolean result = installment.isPaymentBeforeThreeMonthsThanDueDate(paymentDate);

        // Then
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenPaymentIsBeforeDueDate() {
        // given
        Installment installment = Installment.builder()
                .dueDate(LocalDate.of(2025, 6, 1))
                .build();
        LocalDate paymentDate = LocalDate.of(2025, 5, 31);

        // when
        boolean result = installment.isPaymentBeforeDueDate(paymentDate);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalse_WhenPaymentIsOnOrAfterDueDate() {
        // given
        Installment installment = Installment.builder()
                .dueDate(LocalDate.of(2025, 6, 1))
                .build();
        LocalDate paymentDate = LocalDate.of(2025, 6, 1);

        // when
        boolean result = installment.isPaymentBeforeDueDate(paymentDate);

        // then
        assertFalse(result);
    }

    @Test
    void shouldReturnTrue_WhenPaymentIsAfterDueDate() {
        // given
        Installment installment = Installment.builder()
                .dueDate(LocalDate.of(2025, 6, 1))
                .build();
        LocalDate paymentDate = LocalDate.of(2025, 6, 2);

        // when
        boolean result = installment.isPaymentAfterDueDate(paymentDate);

        // then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalse_WhenPaymentIsOnOrBeforeDueDate() {
        // given
        Installment installment = Installment.builder()
                .dueDate(LocalDate.of(2025, 6, 1))
                .build();
        LocalDate paymentDate = LocalDate.of(2025, 6, 1);

        // when
        boolean result = installment.isPaymentAfterDueDate(paymentDate);

        // then
        assertFalse(result);
    }

    @Test
    void shouldApplyCorrectDiscount() {
        // given
        BigDecimal amount = new BigDecimal("1000.00");
        LocalDate dueDate = LocalDate.of(2025, 6, 1);
        LocalDate paymentDate = LocalDate.of(2025, 5, 1); // 31 days early
        Installment installment = Installment.builder()
                .amount(amount)
                .dueDate(dueDate)
                .build();

        // when
        BigDecimal discountedAmount = installment.calculateDiscount(amount, paymentDate);

        // then
        BigDecimal expectedDiscount = new BigDecimal("1000.00")
                .multiply(new BigDecimal("0.001"))
                .multiply(new BigDecimal("31"));
        BigDecimal expectedAmount = amount.subtract(expectedDiscount);

        assertEquals(expectedAmount, discountedAmount);
    }

    @Test
    void shouldApplyCorrectPenalty() {
        // given
        BigDecimal amount = new BigDecimal("1000.00");
        LocalDate dueDate = LocalDate.of(2025, 6, 1);
        LocalDate paymentDate = LocalDate.of(2025, 6, 5); // 4 days late
        Installment installment = Installment.builder()
                .amount(amount)
                .dueDate(dueDate)
                .paidAmount(new BigDecimal("1000.00"))
                .build();

        // when
        BigDecimal penaltyAmount = installment.calculatePenalty(amount, paymentDate);

        // then
        BigDecimal expectedPenalty = new BigDecimal("1000.00")
                .multiply(new BigDecimal("0.001"))
                .multiply(new BigDecimal("4"));
        BigDecimal expectedAmount = amount.add(expectedPenalty);

        assertEquals(expectedAmount, penaltyAmount);
    }


}