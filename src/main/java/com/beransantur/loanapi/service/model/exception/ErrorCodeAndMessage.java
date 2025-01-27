package com.beransantur.loanapi.service.model.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeAndMessage {
    USER_ROLE_INVALID(1000, "User with role %s cannot perform this operation"),
    RESOURCE_NOT_FOUND(1001, "%s with id %s could not be found"),
    CUSTOMER_NOT_HAVE_ENOUGH_CREDIT_LIMIT(1002, "Customer does not have enough limit"),
    INVALID_REQUEST(1004, "Invalid request is send"),
    SYSTEM(9999, "There was a system error");

    private final Integer code;
    private final String message;
}
