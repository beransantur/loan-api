package com.beransantur.loanapi.controller;

import com.beransantur.loanapi.controller.dto.CreateLoanRequest;
import com.beransantur.loanapi.controller.dto.CreateLoanResponse;
import com.beransantur.loanapi.service.CustomerLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerLoanController {
    private final CustomerLoanService customerLoanService;

    @PostMapping("/{customerId}/loans")
    public CreateLoanResponse createLoan(@PathVariable Integer customerId, @RequestBody CreateLoanRequest createLoanRequest) {
        return customerLoanService.createLoan(customerId, createLoanRequest);
    }

}
