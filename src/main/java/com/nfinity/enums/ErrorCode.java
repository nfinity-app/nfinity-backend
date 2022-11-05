package com.nfinity.enums;

import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
public enum ErrorCode {
    OK(HttpStatus.SC_OK, "ok"),
    ERROR(HttpStatus.SC_BAD_REQUEST, "bad request"),
    SERVER_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "server error"),

    EMAIL_REGISTERED(100001, "This email has already been registered"),
    NOT_REGISTERED(100002, "This email has not been registered"),
    INCORRECT_INPUT(100003, "Incorrect email or user name or password"),
    INVALID_TOKEN(100004, "Invalid token"),
    INVALID_VERIFICATION_CODE(100005, "Invalid verification code"),
    USERNAME_REGISTERED(100006, "This username has already been registered"),
    INCORRECT_PASSWORD(100007, "Incorrect password"),

    BUSINESS_INFO_NOT_FOUND(100010, "This user's business info not found"),

    LOYALTY_PROGRAM_NOT_FOUND(100020, "This user's loyalty program not found"),
    NONEXISTENT_ORDER(100100, "This order does not exist"),
    NOT_ENOUGH(100101, "nft is not enough"),
    ;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
