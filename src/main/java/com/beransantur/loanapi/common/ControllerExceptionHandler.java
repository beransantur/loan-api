package com.beransantur.loanapi.common;

import com.beransantur.loanapi.controller.dto.BaseResponse;
import com.beransantur.loanapi.service.model.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.beransantur.loanapi.service.model.exception.ErrorCodeAndMessage.INVALID_REQUEST;

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

        e.printStackTrace();

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse<Object> onInvalidFormatException(HttpMessageNotReadableException ex) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(INVALID_REQUEST.getCode())
                .message(ex.getMessage())
                .build();

        return BaseResponse.builder()
                .success(false)
                .error(errorResponse)
                .build();
    }
}
