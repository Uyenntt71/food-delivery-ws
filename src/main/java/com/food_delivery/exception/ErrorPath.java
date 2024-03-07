package com.food_delivery.exception;

import lombok.Getter;

@Getter
public class ErrorPath {
    private final String path;
    private final String message;

    public ErrorPath(final String path, final String message) {
        this.path = path;
        this.message = message;
    }

    public static ErrorPath of(final String path, final String message) {
        return new ErrorPath(path, message);
    }
}
