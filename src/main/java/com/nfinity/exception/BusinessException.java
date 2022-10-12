package com.nfinity.exception;

import com.nfinity.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final ErrorCode errorCode;
    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
