package com.beransantur.loanapi.repository.entity;

import com.beransantur.loanapi.service.model.Loan;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "loan")
@Setter
@Getter
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "installment_number", nullable = false)
    private Integer installmentNumber;

    @Column(name = "is_paid")
    private Boolean isPaid = Boolean.FALSE;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<InstallmentEntity> installments;

    public Loan toModel() {
        Loan loan = Loan.builder()
                .id(id)
                .totalAmount(amount)
                .installmentNumber(installmentNumber)
                .isPaid(isPaid)
                .createdAt(createdAt)
                .build();

        return loan.toBuilder()
                .installments(installments.stream().map((installmentEntity -> installmentEntity.toModel(loan)))
                        .toList()).build();
    }
}
