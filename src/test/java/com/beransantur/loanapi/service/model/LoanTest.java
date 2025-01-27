package com.beransantur.loanapi.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    private Loan loan;

    @BeforeEach
    void setUp() {
        loan = Loan.builder()
                .id(1)
                .installmentNumber(12)
                .createdAt(LocalDate.of(2025, 1, 1))
                .build();
    }

    @Test
    void shouldCalculateTotalAmountCorrectly() {
        // given
        BigDecimal initialAmount = new BigDecimal("1000.00");
        BigDecimal interestRate = new BigDecimal("0.05");

        // when
        loan.setTotalAmountWithInterest(initialAmount, interestRate);

        // then
        assertEquals(new BigDecimal("1050.0000"), loan.getAmount());
    }

    @Test
    void shouldGenerateInstallments() {
        // given
        BigDecimal loanAmount = new BigDecimal("1200.00");
        BigDecimal interestRate = new BigDecimal("0.05"); // 5% interest
        loan.setTotalAmountWithInterest(loanAmount, interestRate);
        loan = loan.toBuilder().installmentNumber(6).createdAt(LocalDate.of(2025, 1, 1)).build();

        // when
        loan.setInstallments();

        // then
        List<Installment> installments = loan.getInstallments();
        assertNotNull(installments);
        assertEquals(6, installments.size(), "Installment count should match installmentNumber");

        BigDecimal expectedInstallmentAmount = loan.getAmount().divide(new BigDecimal(6), 2, RoundingMode.HALF_UP);
        LocalDate expectedDueDate = LocalDate.of(2025, 2, 1);

        for (int i = 0; i < installments.size(); i++) {
            Installment installment = installments.get(i);

            assertEquals(expectedInstallmentAmount, installment.getAmount(), "Installment amount should be correct");
            assertEquals(expectedDueDate, installment.getDueDate(), "Installment due date should match expected date");
            expectedDueDate = expectedDueDate.plusMonths(1); // Update to next month's due date
        }
    }

    @Test
    void shouldReturnTrue_whenAllInstallmentsPaid() {
        // given
        loan.setTotalAmountWithInterest(new BigDecimal("1200.00"), new BigDecimal("0.00"));
        loan.setInstallments();
        loan.getInstallments().forEach(installment -> installment.setIsPaid(Boolean.TRUE));

        // when
        boolean allPaid = loan.isAllInstallmentsPaid();

        // then
        assertTrue(allPaid);
    }

    @Test
    void shouldReturnFalse_whenAllInstallmentsNotPaid() {
        // Arrange
        loan.setTotalAmountWithInterest(new BigDecimal("1200.00"), new BigDecimal("0.00"));
        loan.setInstallments();
        loan.getInstallments().get(0).setIsPaid(Boolean.TRUE);

        // Act
        boolean allPaid = loan.isAllInstallmentsPaid();

        // Assert
        assertFalse(allPaid);
    }

    @Test
    void shouldReturnInstallmentsOrderedByDueDate() {
        // given
        loan.setTotalAmountWithInterest(new BigDecimal("1200.00"), new BigDecimal("0.00"));
        loan.setInstallments();

        // when
        List<Installment> installments = loan.getInstallments();

        // then
        for (int i = 1; i < installments.size(); i++) {
            assertTrue(installments.get(i - 1).getDueDate().isBefore(installments.get(i).getDueDate()));
        }
    }

    @Test
    public void shouldSetIsPaidToTrue() {
        assertEquals(Boolean.FALSE, loan.getIsPaid());
        //when
        loan.setIsPaid(Boolean.TRUE);

        //then
        assertEquals(Boolean.TRUE, loan.getIsPaid());
    }


}