package com.beransantur.loanapi.service;

import com.beransantur.loanapi.controller.dto.PayLoanRequest;
import com.beransantur.loanapi.controller.dto.PayLoanResponse;
import com.beransantur.loanapi.repository.LoanRepository;
import com.beransantur.loanapi.service.model.Installment;
import com.beransantur.loanapi.service.model.Loan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanService loanService;

    @Test
    void shouldPayLoan_whenSufficientAmountForInstallments() {
        // given
        Integer loanId = 1;
        PayLoanRequest payLoanRequest = PayLoanRequest.builder().amount(new BigDecimal("1000")).build();
        Loan loan = Loan.builder()
                .id(loanId)
                .installments(List.of(
                        Installment.builder().id(1).amount(new BigDecimal("500")).dueDate(LocalDate.now().plusDays(1)).build(),
                        Installment.builder().id(2).amount(new BigDecimal("500")).dueDate(LocalDate.now().plusDays(2)).build()
                ))
                .build();
        when(loanRepository.getById(loanId)).thenReturn(loan);

        // when
        PayLoanResponse result = loanService.payLoan(loanId, payLoanRequest);

        // then
        assertNotNull(result);
        assertEquals(new BigDecimal("998.500"), result.getTotalPaidAmount());
        assertEquals(2, result.getPaidInstallmentNumber());
        assertTrue(result.getIsLoanPaid());

        verify(loanRepository, times(1)).update(any(Loan.class)); // update is called
    }

    @Test
    void shouldPayPartialLoan_whenAmountIsPartiallySufficient() {
        // given
        Integer loanId = 1;
        PayLoanRequest payLoanRequest = PayLoanRequest.builder().amount(new BigDecimal("700")).build();
        Loan loan = Loan.builder()
                .id(loanId)
                .installments(List.of(
                        Installment.builder().id(1).amount(new BigDecimal("500")).dueDate(LocalDate.now().plusDays(1)).build(),
                        Installment.builder().id(2).amount(new BigDecimal("500")).dueDate(LocalDate.now().plusDays(2)).build()
                ))
                .build();
        when(loanRepository.getById(loanId)).thenReturn(loan);

        // when
        PayLoanResponse result = loanService.payLoan(loanId, payLoanRequest);

        // then
        assertNotNull(result);
        assertEquals(new BigDecimal("499.500"), result.getTotalPaidAmount());
        assertEquals(1, result.getPaidInstallmentNumber()); // Only one installment paid
        assertFalse(result.getIsLoanPaid()); // Loan is not fully paid

        verify(loanRepository, times(1)).update(any(Loan.class)); // update is called
    }

    @Test
    void shouldNotPayLoan_whenAmountIsNotEnoughForInstallment() {
        // given
        Integer loanId = 1;
        PayLoanRequest payLoanRequest = PayLoanRequest.builder().amount(new BigDecimal("200")).build();
        Loan loan = Loan.builder()
                .id(loanId)
                .installments(List.of(
                        Installment.builder().id(1).amount(new BigDecimal("500")).dueDate(LocalDate.now().plusDays(1)).build(),
                        Installment.builder().id(2).amount(new BigDecimal("500")).dueDate(LocalDate.now().plusDays(2)).build()
                ))
                .build();
        when(loanRepository.getById(loanId)).thenReturn(loan);

        // when
        PayLoanResponse result = loanService.payLoan(loanId, payLoanRequest);

        // then
        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getTotalPaidAmount());
        assertEquals(0, result.getPaidInstallmentNumber());
        assertFalse(result.getIsLoanPaid());

        verify(loanRepository, times(0)).update(any(Loan.class));
    }

    @Test
    void shouldApplyDiscount_whenPaymentIsMadeBeforeDueDate() {
        // given
        Integer loanId = 1;
        PayLoanRequest payLoanRequest = PayLoanRequest.builder().amount(new BigDecimal("500")).build();
        Loan loan = Loan.builder()
                .id(loanId)
                .installments(List.of(
                        Installment.builder().id(1).amount(new BigDecimal("500")).dueDate(LocalDate.now().plusDays(2)).build()
                ))
                .build();
        when(loanRepository.getById(loanId)).thenReturn(loan);

        // when
        PayLoanResponse result = loanService.payLoan(loanId, payLoanRequest);

        // then
        assertNotNull(result);
        assertTrue(result.getTotalPaidAmount().compareTo(new BigDecimal("500")) < 0); // Amount with discount
        assertEquals(1, result.getPaidInstallmentNumber());
        assertTrue(result.getIsLoanPaid()); // Loan fully paid

        verify(loanRepository, times(1)).update(any(Loan.class)); // update is called
    }

    @Test
    void shouldApplyPenalty_whenPaymentIsMadeAfterDueDate() {
        // given
        Integer loanId = 1;
        PayLoanRequest payLoanRequest = PayLoanRequest.builder().amount(new BigDecimal("500")).build();
        Loan loan = Loan.builder()
                .id(loanId)
                .amount(new BigDecimal("500"))
                .installments(List.of(
                        Installment.builder().id(1).amount(new BigDecimal("500")).dueDate(LocalDate.now().minusDays(2)).build()
                ))
                .build();
        when(loanRepository.getById(loanId)).thenReturn(loan);

        // when
        PayLoanResponse result = loanService.payLoan(loanId, payLoanRequest);

        // then
        assertNotNull(result);
        assertTrue(result.getTotalPaidAmount().equals(new BigDecimal("0"))); // Amount with penalty not enough for payment
        assertEquals(0, result.getPaidInstallmentNumber());
        assertFalse(result.getIsLoanPaid()); // Loan not fully paid

        verify(loanRepository, times(0)).update(any(Loan.class)); // update is called
    }
}
