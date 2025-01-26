package com.beransantur.loanapi.service.model.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
    private final Integer code;
    private final String message;

    public BaseException(ErrorCodeAndMessage errorCodeAndMessage) {
        super(errorCodeAndMessage.getMessage());
        this.code = errorCodeAndMessage.getCode();
        this.message = errorCodeAndMessage.getMessage();
    }

    public BaseException(ErrorCodeAndMessage errorCodeAndMessage, String value) {
        super(String.format(errorCodeAndMessage.getMessage(), value));
        this.code = errorCodeAndMessage.getCode();
        this.message = String.format(errorCodeAndMessage.getMessage(), value);
    }

    public BaseException(ErrorCodeAndMessage errorCodeAndMessage, String key, String value) {
        super(String.format(errorCodeAndMessage.getMessage(), key, value));
        this.code = errorCodeAndMessage.getCode();
        this.message = String.format(errorCodeAndMessage.getMessage(), key, value);
    }
}
