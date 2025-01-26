package com.beransantur.loanapi.repository;

import com.beransantur.loanapi.repository.entity.CustomerEntity;
import com.beransantur.loanapi.repository.entity.InstallmentEntity;
import com.beransantur.loanapi.repository.entity.LoanEntity;
import com.beransantur.loanapi.repository.jpa.CustomerJpaRepository;
import com.beransantur.loanapi.repository.jpa.LoanJpaRepository;
import com.beransantur.loanapi.service.model.Customer;
import com.beransantur.loanapi.service.model.Installment;
import com.beransantur.loanapi.service.model.Loan;
import com.beransantur.loanapi.service.model.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {
    private final CustomerJpaRepository customerJpaRepository;
    private final LoanJpaRepository loanJpaRepository;

    public Customer getById(Integer id) {
        return customerJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("customer", id))
                .toModel();
    }

    public List<Loan> retrieveLoans(Integer customerId) {
        CustomerEntity customer = customerJpaRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("customer", customerId));

        return customer.getLoans().stream()
                .map(LoanEntity::toModel)
                .toList();
    }

    public Integer saveLoan(Customer customer, Loan loan) {
        CustomerEntity customerEntity = customerJpaRepository.findById(customer.getId())
                .orElseThrow(() -> new NotFoundException("customer", customer.getId()));
        customerEntity.updateFromModel(customer);

        LoanEntity loanEntity = buildLoanEntity(loan, customerEntity);
        List<InstallmentEntity> installmentEntities = new ArrayList<>();
        for (Installment installment : loan.getInstallments()) {
            InstallmentEntity installmentEntity = buildInstallmentEntity(installment, loanEntity);
            installmentEntities.add(installmentEntity);
        }
        loanEntity.setInstallments(installmentEntities);

        customerEntity.getLoans().add(loanEntity);

        loanJpaRepository.save(loanEntity);
        customerJpaRepository.save(customerEntity);
        return loanEntity.getId();
    }

    private LoanEntity buildLoanEntity(Loan loan, CustomerEntity customerEntity) {
        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setAmount(loan.getTotalAmount());
        loanEntity.setCustomer(customerEntity);
        loanEntity.setIsPaid(loan.getIsPaid());
        loanEntity.setInstallmentNumber(loan.getInstallmentNumber());
        loanEntity.setCreatedAt(loan.getCreatedAt());
        return loanEntity;
    }

    private InstallmentEntity buildInstallmentEntity(Installment installment, LoanEntity loanEntity) {
        InstallmentEntity installmentEntity = new InstallmentEntity();
        installmentEntity.setAmount(installment.getAmount());
        installmentEntity.setDueDate(installment.getDueDate());
        installmentEntity.setIsPaid(installment.getIsPaid());
        installmentEntity.setPaymentDate(installment.getPaymentDate());
        installmentEntity.setPaidAmount(installment.getPaidAmount());
        installmentEntity.setLoan(loanEntity);
        return installmentEntity;
    }
}
