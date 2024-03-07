package com.food_delivery.exception;

import lombok.Getter;

@Getter
public class CommonException extends Exception {
    private ErrorCode errorCode;
    private String code;

    public CommonException() { super(); }

    public CommonException(ErrorCode errorCode) { this.errorCode = errorCode; }

    public CommonException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CommonException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public CommonException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public CommonException(String message, String code, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.code = code;
    }
}
