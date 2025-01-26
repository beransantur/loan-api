package com.beransantur.loanapi.repository.entity;

import com.beransantur.loanapi.service.model.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "customer")
@Setter
@Getter
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "credit_limit")
    private BigDecimal creditLimit;

    @Column(name = "used_credit_limit")
    private BigDecimal usedCreditLimit;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    private List<LoanEntity> loans;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity userDetail;

    public Customer toModel() {
        return Customer.builder()
                .name(userDetail.getName())
                .surname(userDetail.getSurname())
                .creditLimit(creditLimit)
                .usedCreditLimit(usedCreditLimit)
                .loans(loans.stream().map(LoanEntity::toModel).collect(Collectors.toCollection(ArrayList::new)))
                .build();
    }
}
