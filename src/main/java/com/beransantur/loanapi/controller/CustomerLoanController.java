package com.beransantur.loanapi.controller;

import com.beransantur.loanapi.controller.dto.CreateLoanRequest;
import com.beransantur.loanapi.controller.dto.CreateLoanResponse;
import com.beransantur.loanapi.controller.dto.RetrieveLoanResponse;
import com.beransantur.loanapi.service.CustomerLoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerLoanController {
    private final CustomerLoanService customerLoanService;

    @GetMapping("/{customerId}/loans")
    @PreAuthorize("hasAuthority('read-loan') or hasAuthority('create-loan')")
    public List<RetrieveLoanResponse> retrieveLoans(@PathVariable Integer customerId) {
        return customerLoanService.retrieveLoans(customerId);
    }

    @PostMapping("/{customerId}/loans")
    @PreAuthorize("hasAuthority('create-loan')")
    public CreateLoanResponse createLoan(@PathVariable Integer customerId, @RequestBody @Valid CreateLoanRequest createLoanRequest) {
        return customerLoanService.createLoan(customerId, createLoanRequest);
    }

}
