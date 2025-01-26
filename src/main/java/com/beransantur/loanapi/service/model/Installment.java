package com.beransantur.loanapi.service.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class Installment {
    private Integer id;
    private Loan loan;
    private BigDecimal amount;
    private BigDecimal paidAmount;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    @Builder.Default
    private Boolean isPaid = Boolean.FALSE;

}
