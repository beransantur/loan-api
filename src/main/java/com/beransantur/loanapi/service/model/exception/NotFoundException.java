package com.beransantur.loanapi.service.model.exception;

public class NotFoundException extends BaseException {
    public NotFoundException(String resourceName, Integer id) {
        super(ErrorCodeAndMessage.RESOURCE_NOT_FOUND, resourceName, String.valueOf(id));
    }
}
