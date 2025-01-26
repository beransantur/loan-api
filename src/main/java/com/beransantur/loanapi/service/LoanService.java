package com.beransantur.loanapi.service;

import com.beransantur.loanapi.controller.dto.RetrieveInstallmentResponse;
import com.beransantur.loanapi.repository.LoanRepository;
import com.beransantur.loanapi.service.model.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;

    public List<RetrieveInstallmentResponse> retrieveInstallments(Integer loanId) {
        Loan loan = loanRepository.getById(loanId);
        return loan.getInstallments().stream().map((RetrieveInstallmentResponse::fromModel)).toList();
    }

}
