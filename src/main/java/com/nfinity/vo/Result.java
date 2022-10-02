package com.nfinity.vo;

import com.nfinity.enums.ErrorCode;

public class Result<T> {
    private int code;
    private String message;
    private T data;

    public Result() {
    }

    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Result fail(int code, String message){
        return new Result(code, message);
    }

    public Result<T> succeed(ErrorCode errorCode, T data){
        return new Result<>(errorCode.getCode(), data);
    }
}
