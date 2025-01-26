package com.beransantur.loanapi.config;

import com.beransantur.loanapi.controller.dto.BaseResponse;
import com.beransantur.loanapi.service.model.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BaseResponse<Object> onRuntimeException(RuntimeException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCodeAndMessage.SYSTEM.getCode())
                .message(ErrorCodeAndMessage.SYSTEM.getMessage())
                .build();

        log.error(ErrorCodeAndMessage.SYSTEM.getMessage());

        return BaseResponse.builder()
                .success(false)
                .error(errorResponse)
                .build();
    }

    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BaseResponse<Object> onBaseException(BaseException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .build();

        log.error(e.getMessage());

        return BaseResponse.builder()
                .success(false)
                .error(errorResponse)
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public BaseResponse<Object> onNotFoundException(NotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .build();

        log.error(e.getMessage());

        return BaseResponse.builder()
                .success(false)
                .error(errorResponse)
                .build();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse<Object> onValidationException(ValidationException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .build();

        log.error(e.getMessage());

        return BaseResponse.builder()
                .success(false)
                .error(errorResponse)
                .build();
    }
}
