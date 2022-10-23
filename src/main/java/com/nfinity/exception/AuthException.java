package com.nfinity.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException{
    private final String message;

    public AuthException(String message){
        this.message = message;
    }
}
