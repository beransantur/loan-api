package com.beransantur.loanapi.service.model.exception;

public class ValidationException extends BaseException {
    public ValidationException(ErrorCodeAndMessage errorCodeAndMessage, String key) {
        super(errorCodeAndMessage, key);
    }

    public ValidationException(ErrorCodeAndMessage errorCodeAndMessage) {
        super(errorCodeAndMessage);
    }
}
