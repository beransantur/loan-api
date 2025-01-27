package com.beransantur.loanapi.repository;


import com.beransantur.loanapi.repository.entity.InstallmentEntity;
import com.beransantur.loanapi.repository.entity.LoanEntity;
import com.beransantur.loanapi.repository.jpa.LoanJpaRepository;
import com.beransantur.loanapi.service.model.Installment;
import com.beransantur.loanapi.service.model.Loan;
import com.beransantur.loanapi.service.model.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanRepositoryTest {

    @Mock
    private LoanJpaRepository loanJpaRepository;

    @InjectMocks
    private LoanRepository loanRepository;

    private LoanEntity loanEntity;
    private Loan loan;
    private InstallmentEntity installmentEntity;
    private Installment installment;

    @BeforeEach
    public void setUp() {
        // Initialize LoanEntity and InstallmentEntity
        loanEntity = new LoanEntity();
        loanEntity.setId(1);
        loanEntity.setAmount(BigDecimal.valueOf(5000));
        loanEntity.setInstallmentNumber(12);
        loanEntity.setIsPaid(false);

        installmentEntity = new InstallmentEntity();
        installmentEntity.setId(1);
        installmentEntity.setAmount(BigDecimal.valueOf(1000));
        installmentEntity.setDueDate(LocalDate.now().plusMonths(1));
        installmentEntity.setIsPaid(false);

        loanEntity.setInstallments(List.of(installmentEntity));

        // Initialize Loan and Installment
        installment = Installment.builder()
                .id(1)
                .amount(BigDecimal.valueOf(1000))
                .dueDate(LocalDate.now().plusMonths(1))
                .isPaid(true)
                .paidAmount(BigDecimal.valueOf(500))
                .paymentDate(LocalDate.now().plusDays(5))
                .build();

        loan = Loan.builder()
                .id(1)
                .amount(BigDecimal.valueOf(5000))
                .installmentNumber(12)
                .installments(List.of(installment))
                .isPaid(false)
                .build();
    }

    @Test
    public void shouldReturnLoan_whenLoanExists() {
        // given
        Integer loanId = 1;
        loanEntity.setId(loanId);
        loanEntity.setAmount(BigDecimal.valueOf(5000));
        loanEntity.setInstallmentNumber(12);
        loanEntity.setIsPaid(false);

        InstallmentEntity installmentEntity1 = new InstallmentEntity();
        installmentEntity1.setId(1);
        installmentEntity1.setAmount(BigDecimal.valueOf(1000));
        installmentEntity1.setDueDate(LocalDate.now().plusMonths(1));
        installmentEntity1.setIsPaid(false);
        loanEntity.setInstallments(List.of(installmentEntity1));

        when(loanJpaRepository.findById(loanId)).thenReturn(Optional.of(loanEntity)); // Correct usage of Optional

        // when
        Loan result = loanRepository.getById(loanId);

        // then
        assertNotNull(result);
        assertEquals(loanId, result.getId());
        assertEquals(loanEntity.getAmount(), result.getAmount());
        assertEquals(loanEntity.getInstallments().size(), result.getInstallments().size());
    }

    @Test
    public void shouldThrowNotFoundException_whenLoanDoesNotExist() {
        // given
        Integer loanId = 1;
        when(loanJpaRepository.findById(loanId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> loanRepository.getById(loanId));
    }

    @Test
    public void shouldUpdateLoan_whenLoanExists() {
        // given
        Integer loanId = 1;

        // Populating LoanEntity
        loanEntity.setId(loanId);
        loanEntity.setAmount(BigDecimal.valueOf(5000));
        loanEntity.setInstallmentNumber(12);
        loanEntity.setIsPaid(false);

        InstallmentEntity installmentEntity1 = new InstallmentEntity();
        installmentEntity1.setId(1);
        installmentEntity1.setAmount(BigDecimal.valueOf(1000));
        installmentEntity1.setDueDate(LocalDate.now().plusMonths(1));
        installmentEntity1.setIsPaid(false);
        loanEntity.setInstallments(List.of(installmentEntity1));

        when(loanJpaRepository.findById(loanId)).thenReturn(Optional.of(loanEntity));

        // when
        loanRepository.update(loan);

        // then
        assertEquals(installment.getIsPaid(), loanEntity.getInstallments().get(0).getIsPaid());
        assertEquals(installment.getPaidAmount(), loanEntity.getInstallments().get(0).getPaidAmount());
        assertEquals(installment.getPaymentDate(), loanEntity.getInstallments().get(0).getPaymentDate());
        verify(loanJpaRepository, times(1)).save(loanEntity);
    }
}
