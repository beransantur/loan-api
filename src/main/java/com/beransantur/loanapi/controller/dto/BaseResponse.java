package com.beransantur.loanapi.controller.dto;

import com.beransantur.loanapi.service.model.exception.ErrorResponse;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class BaseResponse<T> {
    @Builder.Default
    boolean success = true;
    ErrorResponse error;
    T data;
}