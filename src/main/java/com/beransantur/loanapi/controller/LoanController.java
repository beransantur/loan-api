package com.beransantur.loanapi.controller;

import com.beransantur.loanapi.controller.dto.RetrieveInstallmentResponse;
import com.beransantur.loanapi.controller.dto.RetrieveLoanResponse;
import com.beransantur.loanapi.service.CustomerService;
import com.beransantur.loanapi.service.LoanService;
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
    @PreAuthorize("hasAuthority('read-installments')")
    public List<RetrieveInstallmentResponse> retrieveInstallments(@PathVariable Integer loanId) {
        return loanService.retrieveInstallments(loanId);
    }

}
