package com.beransantur.loanapi.service;

import com.beransantur.loanapi.controller.dto.CreateLoanRequest;
import com.beransantur.loanapi.controller.dto.CreateLoanResponse;
import com.beransantur.loanapi.repository.CustomerLoanRepository;
import com.beransantur.loanapi.service.model.Customer;
import com.beransantur.loanapi.service.model.Loan;
import com.beransantur.loanapi.service.model.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.beransantur.loanapi.service.model.exception.ErrorCodeAndMessage.CUSTOMER_NOT_HAVE_ENOUGH_CREDIT_LIMIT;

@Service
@RequiredArgsConstructor
public class CustomerLoanService {
    private final CustomerService customerService;
    private final CustomerLoanRepository customerLoanRepository;

    public CreateLoanResponse createLoan(Integer customerId, CreateLoanRequest createLoanRequest) {
        Loan loan = createLoanRequest.toModel();
        loan.addInstallments();

        Customer customer = customerService.getCustomerById(customerId);
        if (!customer.hasEnoughLimit(loan.getTotalAmount())) {
            throw new ValidationException(CUSTOMER_NOT_HAVE_ENOUGH_CREDIT_LIMIT);
        }

        Integer savedLoanId = customerLoanRepository.saveLoan(customerId, loan);
        return new CreateLoanResponse(savedLoanId);
    }
}
