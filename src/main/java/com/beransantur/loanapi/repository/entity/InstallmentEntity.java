package com.beransantur.loanapi.repository.entity;

import com.beransantur.loanapi.service.model.Installment;
import com.beransantur.loanapi.service.model.Loan;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "installment")
@Setter
@Getter
public class InstallmentEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id", nullable = false)
    private LoanEntity loan;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "paid_amount")
    private BigDecimal paidAmount;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "is_paid")
    private Boolean isPaid = Boolean.FALSE;

    public Installment toModel(Loan loan) {
        return Installment.builder()
                .id(id)
                .loan(loan)
                .amount(amount)
                .paidAmount(paidAmount)
                .dueDate(dueDate)
                .paymentDate(paymentDate)
                .isPaid(isPaid)
                .build();
    }

}
