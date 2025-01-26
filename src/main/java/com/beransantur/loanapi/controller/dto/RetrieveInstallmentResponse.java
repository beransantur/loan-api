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
    private BigDecimal amount;
    private BigDecimal paidAmount;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private Boolean isPaid;

    public static RetrieveInstallmentResponse fromModel(Installment installment) {
        return RetrieveInstallmentResponse.builder()
                .amount(installment.getAmount())
                .paidAmount(installment.getPaidAmount())
                .dueDate(installment.getDueDate())
                .paymentDate(installment.getPaymentDate())
                .isPaid(installment.getIsPaid())
                .build();
    }

}
