package com.nfinity.enums;

import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
public enum ErrorCode {
    OK(HttpStatus.SC_OK, "ok"),
    ERROR(HttpStatus.SC_BAD_REQUEST, "bad request"),
    NOT_FOUND(HttpStatus.SC_NOT_FOUND, "not found"),
    CONFLICT(HttpStatus.SC_CONFLICT, "conflict"),
    UNAUTHORIZED(HttpStatus.SC_UNAUTHORIZED, "unauthorized"),
    SERVER_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "server error"),
    ;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
