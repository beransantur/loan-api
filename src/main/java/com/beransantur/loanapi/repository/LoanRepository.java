package com.beransantur.loanapi.repository;

import com.beransantur.loanapi.repository.entity.InstallmentEntity;
import com.beransantur.loanapi.repository.entity.LoanEntity;
import com.beransantur.loanapi.repository.jpa.LoanJpaRepository;
import com.beransantur.loanapi.service.model.Installment;
import com.beransantur.loanapi.service.model.Loan;
import com.beransantur.loanapi.service.model.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class LoanRepository {
    private final LoanJpaRepository loanJpaRepository;

    public Loan getById(Integer id) {
        return loanJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("loan", id))
                .toModel();

    }

    public void update(Loan loan) {
        LoanEntity loanEntity = loanJpaRepository.findById(loan.getId())
                .orElseThrow(() -> new NotFoundException("loan", loan.getId()));

        updateLoanEntity(loanEntity, loan);
        loanJpaRepository.save(loanEntity);
    }

    private void updateLoanEntity(LoanEntity loanEntity, Loan loan) {
        loanEntity.setIsPaid(loan.getIsPaid());

        Map<Integer, Installment> indexAndInstallment = new HashMap<>();
        for (Installment installment : loan.getInstallments()) {
            indexAndInstallment.put(installment.getId(), installment);
        }

        for (InstallmentEntity installmentEntity : loanEntity.getInstallments()) {
            Installment installment = indexAndInstallment.get(installmentEntity.getId());
            installmentEntity.setIsPaid(installment.getIsPaid());
            installmentEntity.setPaidAmount(installment.getPaidAmount());
            installmentEntity.setPaymentDate(installment.getPaymentDate());
        }
    }
}
