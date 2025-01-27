package com.beransantur.loanapi.repository;

import com.beransantur.loanapi.repository.entity.CustomerEntity;
import com.beransantur.loanapi.repository.entity.InstallmentEntity;
import com.beransantur.loanapi.repository.entity.LoanEntity;
import com.beransantur.loanapi.repository.entity.UserEntity;
import com.beransantur.loanapi.repository.jpa.CustomerJpaRepository;
import com.beransantur.loanapi.repository.jpa.LoanJpaRepository;
import com.beransantur.loanapi.service.model.Customer;
import com.beransantur.loanapi.service.model.Installment;
import com.beransantur.loanapi.service.model.Loan;
import com.beransantur.loanapi.service.model.exception.NotFoundException;
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
class CustomerRepositoryTest {

    @Mock
    private CustomerJpaRepository customerJpaRepository;

    @Mock
    private LoanJpaRepository loanJpaRepository;

    @InjectMocks
    private CustomerRepository customerRepository;

    @Test
    public void shouldReturnCustomer_whenCustomerExists() {
        // given
        Integer customerId = 1;
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customerId);
        customerEntity.setCreditLimit(BigDecimal.valueOf(10000));
        customerEntity.setUsedCreditLimit(BigDecimal.valueOf(2000));

        UserEntity userDetailsEntity = new UserEntity();
        userDetailsEntity.setName("John");
        userDetailsEntity.setSurname("Doe");
        customerEntity.setUserDetail(userDetailsEntity);

        when(customerJpaRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));

        Customer customer = Customer.builder()
                .id(customerEntity.getId())
                .name(customerEntity.getUserDetail().getName())
                .surname(customerEntity.getUserDetail().getSurname())
                .creditLimit(customerEntity.getCreditLimit())
                .usedCreditLimit(customerEntity.getUsedCreditLimit())
                .build();

        // when
        Customer result = customerRepository.getById(customerId);

        // then
        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getSurname());
    }

    @Test
    public void shouldThrowNotFoundException_whenCustomerNotFound() {
        // given
        Integer customerId = 1;
        when(customerJpaRepository.findById(customerId)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> customerRepository.getById(customerId));
    }

    @Test
    public void shouldRetrieveLoans_whenCustomerHasLoans() {
        // given
        Integer customerId = 1;
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customerId);

        InstallmentEntity installmentEntity1 = new InstallmentEntity();
        installmentEntity1.setId(1);
        installmentEntity1.setAmount(BigDecimal.valueOf(1000));
        installmentEntity1.setDueDate(LocalDate.now().plusMonths(1));
        installmentEntity1.setIsPaid(false);

        InstallmentEntity installmentEntity2 = new InstallmentEntity();
        installmentEntity2.setId(2);
        installmentEntity2.setAmount(BigDecimal.valueOf(1000));
        installmentEntity2.setDueDate(LocalDate.now().plusMonths(2));
        installmentEntity2.setIsPaid(false);

        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setId(1);
        loanEntity.setAmount(BigDecimal.valueOf(5000));
        loanEntity.setInstallmentNumber(12);
        loanEntity.setIsPaid(false);
        loanEntity.setInstallments(List.of(installmentEntity1, installmentEntity2));

        List<LoanEntity> loanEntities = List.of(loanEntity);
        customerEntity.setLoans(loanEntities);

        when(customerJpaRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));

        // when
        List<Loan> loans = customerRepository.retrieveLoans(customerId);

        // then
        assertNotNull(loans);
        assertEquals(1, loans.size());
        assertEquals(BigDecimal.valueOf(5000), loans.get(0).getAmount());
        assertEquals(2, loans.get(0).getInstallments().size()); // Ensure installments are retrieved
        assertEquals(BigDecimal.valueOf(1000), loans.get(0).getInstallments().get(0).getAmount());
    }

    @Test
    public void shouldSaveLoan_whenLoanIsCreated() {
        // given
        Integer customerId = 1;
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customerId);
        customerEntity.setCreditLimit(BigDecimal.valueOf(10000));
        customerEntity.setUsedCreditLimit(BigDecimal.valueOf(2000));

        UserEntity userDetailsEntity = new UserEntity();
        userDetailsEntity.setName("John");
        userDetailsEntity.setSurname("Doe");
        customerEntity.setUserDetail(userDetailsEntity);

        Loan loan = Loan.builder()
                .amount(BigDecimal.valueOf(5000))
                .installmentNumber(12)
                .installments(List.of(
                        Installment.builder()
                                .amount(BigDecimal.valueOf(1000))
                                .dueDate(LocalDate.now().plusMonths(1))
                                .isPaid(false)
                                .build(),
                        Installment.builder()
                                .amount(BigDecimal.valueOf(1000))
                                .dueDate(LocalDate.now().plusMonths(2))
                                .isPaid(false)
                                .build()
                ))
                .build();

        when(customerJpaRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));

        Customer customer = Customer.builder()
                .id(customerEntity.getId())
                .name(customerEntity.getUserDetail().getName())
                .surname(customerEntity.getUserDetail().getSurname())
                .creditLimit(customerEntity.getCreditLimit())
                .usedCreditLimit(customerEntity.getUsedCreditLimit())
                .build();

        // when
        customerRepository.saveLoan(customer, loan);

        // then
        verify(customerJpaRepository, times(1)).save(any(CustomerEntity.class));
        verify(loanJpaRepository, times(1)).save(any(LoanEntity.class));
    }

    @Test
    public void shouldThrowNotFoundException_whenCustomerNotFoundForLoanSave() {
        // given
        Integer customerId = 1;
        Loan loan = Loan.builder()
                .amount(BigDecimal.valueOf(5000))
                .installmentNumber(12)
                .installments(List.of(
                        Installment.builder()
                                .amount(BigDecimal.valueOf(1000))
                                .dueDate(LocalDate.now().plusMonths(1))
                                .isPaid(false)
                                .build(),
                        Installment.builder()
                                .amount(BigDecimal.valueOf(1000))
                                .dueDate(LocalDate.now().plusMonths(2))
                                .isPaid(false)
                                .build()
                ))
                .build();

        when(customerJpaRepository.findById(customerId)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> customerRepository.saveLoan(Customer.builder().id(customerId).build(), loan));
    }

}
