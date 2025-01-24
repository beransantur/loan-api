package com.beransantur.loanapi.customer.service;

import com.beransantur.loanapi.customer.controller.dto.CreateLoanRequest;
import com.beransantur.loanapi.customer.controller.dto.RetrieveLoanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    public RetrieveLoanResponse getLoansByCustomerId(Long customerId, CreateLoanRequest createLoanRequest) {

        return null;
    }
}
