package com.beransantur.loanapi.controller.dto;

import com.beransantur.loanapi.service.model.Installment;
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
public class RetrieveInstallmentResponse {
    private Integer id;
    private LocalDate dueDate;
    private BigDecimal amount;
    private Boolean isPaid;
    private BigDecimal paidAmount;
    private LocalDate paymentDate;

    public static RetrieveInstallmentResponse fromModel(Installment installment) {
        return RetrieveInstallmentResponse.builder()
                .id(installment.getId())
                .amount(installment.getAmount())
                .paidAmount(installment.getPaidAmount())
                .dueDate(installment.getDueDate())
                .paymentDate(installment.getPaymentDate())
                .isPaid(installment.getIsPaid())
                .build();
    }

}
