package com.beransantur.loanapi.controller;

import com.beransantur.loanapi.controller.dto.BaseResponse;
import com.beransantur.loanapi.controller.dto.PayLoanRequest;
import com.beransantur.loanapi.controller.dto.PayLoanResponse;
import com.beransantur.loanapi.controller.dto.RetrieveInstallmentResponse;
import com.beransantur.loanapi.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @GetMapping("/{loanId}/installments")
    @PreAuthorize("hasAuthority('read-installments') or hasAuthority('pay-loan')")
    public BaseResponse<List<RetrieveInstallmentResponse>> retrieveInstallments(@PathVariable Integer loanId) {
        List<RetrieveInstallmentResponse> retrieveInstallmentResponses = loanService.retrieveInstallments(loanId);

        return BaseResponse.<List<RetrieveInstallmentResponse>>builder()
                .data(retrieveInstallmentResponses)
                .build();
    }

    @PostMapping("/{loanId}/payment")
    @PreAuthorize("hasAuthority('pay-loan')")
    public BaseResponse<PayLoanResponse> payLoan(@PathVariable Integer loanId, @RequestBody @Valid PayLoanRequest payLoanRequest) {
        PayLoanResponse payLoanResponse = loanService.payLoan(loanId, payLoanRequest);

        return BaseResponse.<PayLoanResponse>builder()
                .data(payLoanResponse)
                .build();
    }

}
