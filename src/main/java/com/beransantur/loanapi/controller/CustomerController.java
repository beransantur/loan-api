package com.beransantur.loanapi.controller;

import com.beransantur.loanapi.controller.dto.BaseResponse;
import com.beransantur.loanapi.controller.dto.CreateLoanRequest;
import com.beransantur.loanapi.controller.dto.CreateLoanResponse;
import com.beransantur.loanapi.controller.dto.RetrieveLoanResponse;
import com.beransantur.loanapi.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/{customerId}/loans")
    @PreAuthorize("hasAuthority('read-loan') or hasAuthority('create-loan')")
    public BaseResponse<List<RetrieveLoanResponse>> retrieveLoans(@PathVariable Integer customerId) {
        List<RetrieveLoanResponse> retrieveLoanResponses = customerService.retrieveLoans(customerId);

        return BaseResponse.<List<RetrieveLoanResponse>>builder()
                .data(retrieveLoanResponses)
                .build();
    }

    @PostMapping("/{customerId}/loans")
    @PreAuthorize("hasAuthority('create-loan')")
    public BaseResponse<CreateLoanResponse> createLoan(@PathVariable Integer customerId, @Valid @RequestBody CreateLoanRequest createLoanRequest) {
        CreateLoanResponse createLoanResponse = customerService.createLoan(customerId, createLoanRequest);

        return BaseResponse.<CreateLoanResponse>builder()
                .data(createLoanResponse)
                .build();
    }

}
