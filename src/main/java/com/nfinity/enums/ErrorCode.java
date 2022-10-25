package com.nfinity.enums;

import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
public enum ErrorCode {
    OK(HttpStatus.SC_OK, "ok"),
    ERROR(HttpStatus.SC_BAD_REQUEST, "bad request"),
    SERVER_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "server error"),
    REGISTERED(100001, "This email has already been registered"),
    NOT_REGISTERED(100002, "This email has not been registered"),
    INCORRECT_INPUT(100003, "Incorrect email or user name or password"),
    INVALID_TOKEN(100004, "Invalid token"),
    NOT_ENOUGH(100010, "nft is not enough"),

    NONEXISTENT_ORDER(100011, "This order does not exist"),
    ;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
