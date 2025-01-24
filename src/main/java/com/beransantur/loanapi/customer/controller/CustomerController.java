package com.beransantur.loanapi.customer.controller;

import com.beransantur.loanapi.customer.controller.dto.CreateLoanRequest;
import com.beransantur.loanapi.customer.controller.dto.CreateLoanResponse;
import com.beransantur.loanapi.customer.controller.dto.RetrieveLoanResponse;
import com.beransantur.loanapi.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    //private final RetrieveDepartmentInfoService retrieveDepartmentInfoService;

    @GetMapping("/{id}/loans")
    @PreAuthorize("hasAuthority('customers')")
    public RetrieveLoanResponse getLoansByCustomerId(@PathVariable Long id) {
        return null;
        //return retrieveDepartmentInfoService.retrieve(id);
    }

    @PostMapping("/{id}/loans")
    @PreAuthorize("hasAuthority('customers')")
    public CreateLoanResponse createLoanByCustomerId(@PathVariable Long id, @RequestBody CreateLoanRequest createLoanRequest) {
        return null;
        //return retrieveDepartmentInfoService.retrieve(id);
    }

}
