package com.nfinity.exception;

import com.nfinity.enums.ErrorCode;
import com.nfinity.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public <T> Result<T> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String message = allErrors.stream().map(error -> {
            if(error instanceof FieldError){
                return ((FieldError) error).getField() + " " + error.getDefaultMessage();
            }else {
                return error.getDefaultMessage();
            }
        }).collect(Collectors.joining(";"));
        return Result.fail(ErrorCode.ERROR.getCode(), message);
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public <T> Result<T> handleBusinessException(BusinessException e){
        return Result.fail(e.getErrorCode());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public <T> Result<T> handleCommonException(Exception e){
        log.error("common exception", e);
        return Result.fail(ErrorCode.SERVER_ERROR.getCode(), e.getMessage());
    }
}
