package com.food_delivery.exception;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Data;
import lombok.Getter;

@Getter
public enum ErrorCode {
    GENERAL(2),
    AUTHENTICATION(10),
    PERMISSION_DENIED(20),
    INVALID_ARGUMENTS(30),
    BAD_REQUEST_PARAMS(31),
    ITEM_NOT_FOUND(32),
    EXISTS(33);

    private int errorCode;

    ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @JsonValue
    public int getErrorCode() {
        return errorCode;
    }
}
