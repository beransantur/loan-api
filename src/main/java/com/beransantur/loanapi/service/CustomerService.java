package com.beransantur.loanapi.service;

import com.beransantur.loanapi.controller.dto.CreateLoanRequest;
import com.beransantur.loanapi.controller.dto.CreateLoanResponse;
import com.beransantur.loanapi.controller.dto.RetrieveLoanResponse;
import com.beransantur.loanapi.repository.CustomerRepository;
import com.beransantur.loanapi.service.model.Customer;
import com.beransantur.loanapi.service.model.Loan;
import com.beransantur.loanapi.service.model.exception.ValidationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.beransantur.loanapi.service.model.exception.ErrorCodeAndMessage.CUSTOMER_NOT_HAVE_ENOUGH_CREDIT_LIMIT;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<RetrieveLoanResponse> retrieveLoans(Integer customerId) {
        List<Loan> loans = customerRepository.retrieveLoans(customerId);
        return loans.stream().map(RetrieveLoanResponse::fromModel).toList();
    }

    @Transactional
    public CreateLoanResponse createLoan(Integer customerId, CreateLoanRequest createLoanRequest) {
        Loan loan = createLoanRequest.toModel();
        loan.setTotalAmountWithInterest(createLoanRequest.getAmount(), createLoanRequest.getInterestRate());
        loan.setInstallments();

        Customer customer = customerRepository.getById(customerId);
        if (!customer.hasEnoughLimit(loan.getAmount())) {
            throw new ValidationException(CUSTOMER_NOT_HAVE_ENOUGH_CREDIT_LIMIT);
        }
        customer.reduceCreditLimit(loan.getAmount());

        Integer savedLoanId = customerRepository.saveLoan(customer, loan);

        log.info("Loan is successfully created with id {}", savedLoanId);

        return new CreateLoanResponse(savedLoanId);
    }
}
