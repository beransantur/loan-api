package com.beransantur.loanapi.service;

import com.beransantur.loanapi.controller.dto.PayLoanRequest;
import com.beransantur.loanapi.controller.dto.PayLoanResponse;
import com.beransantur.loanapi.controller.dto.RetrieveInstallmentResponse;
import com.beransantur.loanapi.repository.LoanRepository;
import com.beransantur.loanapi.service.model.Installment;
import com.beransantur.loanapi.service.model.Loan;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;

    public List<RetrieveInstallmentResponse> retrieveInstallments(Integer loanId) {
        Loan loan = loanRepository.getById(loanId);
        return loan.getInstallments().stream().map((RetrieveInstallmentResponse::fromModel)).toList();
    }

    @Transactional
    public PayLoanResponse payLoan(Integer loanId, PayLoanRequest payLoanRequest) {
        Loan loan = loanRepository.getById(loanId);
        int paidInstallmentCount = 0;

        List<Installment> installments = loan.getInstallments();

        final LocalDate paymentDate = LocalDate.now();

        BigDecimal demandedPaymentAmount = payLoanRequest.getAmount();
        BigDecimal totalPaidAmount = BigDecimal.ZERO;
        for (Installment installment : installments) {
            BigDecimal paidAmount = installment.getAmount();

            if (installment.getIsPaid().equals(Boolean.TRUE)) {
                continue;
            }

            if (installment.isPaymentBeforeThreeMonthsThanDueDate(paymentDate)) {
                break;
            }

            if (installment.isPaymentBeforeDueDate(paymentDate)) {
                paidAmount = installment.calculateDiscount(paidAmount, paymentDate);
            }

            if (installment.isPaymentAfterDueDate(paymentDate)) {
                paidAmount = installment.calculatePenalty(paidAmount, paymentDate);
            }

            boolean isTotalAmountEnoughForPayment = demandedPaymentAmount.subtract(paidAmount).compareTo(BigDecimal.ZERO) >= 0;
            if (!isTotalAmountEnoughForPayment) {
                break;
            }

            installment.setIsPaid(Boolean.TRUE);
            installment.setPaidAmount(paidAmount);
            installment.setPaymentDate(paymentDate);

            demandedPaymentAmount = demandedPaymentAmount.subtract(paidAmount);
            totalPaidAmount = totalPaidAmount.add(paidAmount);
            paidInstallmentCount++;
        }

        if (loan.isAllInstallmentsPaid()) {
            loan.setIsPaid(Boolean.TRUE);
        }

        loanRepository.update(loan);
        return new PayLoanResponse(totalPaidAmount, paidInstallmentCount, loan.getIsPaid());
    }

}
