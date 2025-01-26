package com.beransantur.loanapi.repository.entity;

import com.beransantur.loanapi.service.model.Installment;
import com.beransantur.loanapi.service.model.Loan;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "customer")
@Setter
@Getter
public class InstallmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id", nullable = false)
    private LoanEntity loan;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "paid_amount")
    private BigDecimal paidAmount;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "is_paid")
    private Boolean isPaid = Boolean.FALSE;

    public Installment toModel(Loan loan) {
        return Installment.builder()
                .loan(loan)
                .amount(amount)
                .paidAmount(paidAmount)
                .dueDate(dueDate)
                .paymentDate(paymentDate)
                .isPaid(isPaid)
                .build();
    }

}
