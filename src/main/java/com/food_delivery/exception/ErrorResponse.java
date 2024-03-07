package com.food_delivery.exception;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;

@Getter
@JsonInclude(Include.NON_NULL)
public class ErrorResponse {
    public ErrorResponse(final String message, final ErrorCode errorCode, final List<ErrorPath> errors, HttpStatus status) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.errors = errors;
        this.timestamp = new Date();
    }

    private HttpStatus status;
    private String message;
    private ErrorCode errorCode;
    private String code;
    private List<ErrorPath> errors;
    private Date timestamp;

    public ErrorResponse(final String message, final ErrorCode errorCode, HttpStatus status) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.timestamp = new Date();
    }

    public ErrorResponse(final String message, String code, final ErrorCode errorCode, HttpStatus status) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.code = code;
        this.timestamp = new Date();
    }

    public static ErrorResponse of(final String message, final ErrorCode errorCode, HttpStatus status) {
        return new ErrorResponse(message, errorCode, status);
    }

    public static ErrorResponse of(final String message, String code, final ErrorCode errorCode, HttpStatus status) {
        return new ErrorResponse(message, code, errorCode, status);
    }

    public static ErrorResponse of(final String message, final ErrorCode errorCode, final List<ErrorPath> errors, HttpStatus status) {
        return new ErrorResponse(message, errorCode, errors, status);
    }
}
