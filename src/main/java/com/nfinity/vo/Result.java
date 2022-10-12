package com.nfinity.vo;

import com.nfinity.enums.ErrorCode;
import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> Result<T> fail(ErrorCode errorCode){
        return new Result<>(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> Result<T> fail(int code, String message){
        return new Result<>(code, message);
    }

    public static <T> Result<T> succeed(ErrorCode errorCode, T data){
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), data);
    }
}
