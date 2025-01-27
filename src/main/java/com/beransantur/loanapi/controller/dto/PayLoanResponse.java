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
public class PayLoanResponse {
    private BigDecimal totalPaidAmount;
    private Integer paidInstallmentNumber;
    private Boolean isLoanPaid;

    public static PayLoanResponse fromModel(Installment installment) {
        return PayLoanResponse.builder()
                .build();
    }

}
