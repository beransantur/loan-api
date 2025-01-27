package com.beransantur.loanapi.controller.dto;

import com.beransantur.loanapi.service.model.Loan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveLoanResponse {
    private Integer id;
    private BigDecimal totalAmount;
    private Integer installmentNumber;
    private Boolean isPaid;
    private LocalDate createdAt;

    public static RetrieveLoanResponse fromModel(Loan loan) {
        return RetrieveLoanResponse.builder()
                .id(loan.getId())
                .totalAmount(loan.getAmount())
                .installmentNumber(loan.getInstallmentNumber())
                .isPaid(loan.getIsPaid())
                .createdAt(loan.getCreatedAt())
                .build();
    }

}
