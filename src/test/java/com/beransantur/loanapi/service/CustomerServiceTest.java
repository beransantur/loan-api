package com.beransantur.loanapi.service;

import com.beransantur.loanapi.controller.dto.CreateLoanRequest;
import com.beransantur.loanapi.controller.dto.CreateLoanResponse;
import com.beransantur.loanapi.controller.dto.InstallmentNumber;
import com.beransantur.loanapi.controller.dto.RetrieveLoanResponse;
import com.beransantur.loanapi.repository.CustomerRepository;
import com.beransantur.loanapi.service.model.Customer;
import com.beransantur.loanapi.service.model.Loan;
import com.beransantur.loanapi.service.model.exception.ErrorCodeAndMessage;
import com.beransantur.loanapi.service.model.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldRetrieveLoans() {
        // given
        Integer customerId = 1;
        Loan loan1 = Loan.builder()
                .id(1)
                .amount(new BigDecimal("1000"))
                .installmentNumber(12)
                .build();
        Loan loan2 = Loan.builder()
                .id(2)
                .amount(new BigDecimal("2000"))
                .installmentNumber(24)
                .build();

        when(customerRepository.retrieveLoans(customerId)).thenReturn(List.of(loan1, loan2));

        // when
        List<RetrieveLoanResponse> responses = customerService.retrieveLoans(customerId);

        // then
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(loan1.getId(), responses.get(0).getId());
        assertEquals(loan2.getId(), responses.get(1).getId());
        verify(customerRepository, times(1)).retrieveLoans(customerId);
    }

    @Test
    void shouldCreateAndSaveLoanSuccessfully() {
        // given
        Integer customerId = 1;
        BigDecimal loanAmount = new BigDecimal("1000.00");
        BigDecimal interestRate = new BigDecimal("0.05");
        InstallmentNumber installmentNumber = InstallmentNumber.TWELVE_MONTHS;

        CreateLoanRequest createLoanRequest = CreateLoanRequest.builder()
                .amount(loanAmount)
                .interestRate(interestRate)
                .installmentNumber(installmentNumber)
                .build();

        Loan loan = createLoanRequest.toModel();
        loan.setTotalAmountWithInterest(loanAmount, interestRate);
        loan.setInstallments();

        Customer customer = mock(Customer.class);
        when(customer.hasEnoughLimit(loan.getAmount())).thenReturn(true);
        when(customerRepository.getById(customerId)).thenReturn(customer);
        when(customerRepository.saveLoan(eq(customer), any(Loan.class))).thenReturn(1);

        // when
        CreateLoanResponse response = customerService.createLoan(customerId, createLoanRequest);

        // then
        assertNotNull(response);
        assertEquals(1, response.loanId());
        verify(customer, times(1)).reduceCreditLimit(loan.getAmount());
        verify(customerRepository, times(1)).getById(customerId);
        verify(customerRepository, times(1)).saveLoan(eq(customer), any(Loan.class));
    }

    @Test
    void shouldThrowException_whenCustomerNotHaveEnoughLimit() {
        // given
        Integer customerId = 1;
        BigDecimal loanAmount = new BigDecimal("3000.00");
        BigDecimal interestRate = new BigDecimal("0.05");
        InstallmentNumber installmentNumber = InstallmentNumber.TWELVE_MONTHS;

        CreateLoanRequest createLoanRequest = CreateLoanRequest.builder()
                .amount(loanAmount)
                .interestRate(interestRate)
                .installmentNumber(installmentNumber)
                .build();

        Loan loan = createLoanRequest.toModel();
        loan.setTotalAmountWithInterest(loanAmount, interestRate);
        loan.setInstallments();

        Customer customer = mock(Customer.class);
        when(customer.hasEnoughLimit(loan.getAmount())).thenReturn(false);
        when(customerRepository.getById(customerId)).thenReturn(customer);

        // when
        ValidationException exception = assertThrows(ValidationException.class, () ->
                customerService.createLoan(customerId, createLoanRequest));

        //given
        assertEquals(ErrorCodeAndMessage.CUSTOMER_NOT_HAVE_ENOUGH_CREDIT_LIMIT.getMessage(), exception.getMessage());
        verify(customerRepository, times(1)).getById(customerId);
        verify(customerRepository, times(0)).saveLoan(any(), any());

    }


}