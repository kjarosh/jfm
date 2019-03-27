package com.github.kjarosh.jfm.api.types;

public class TypeHandlingException extends RuntimeException {
    public TypeHandlingException(String message) {
        super(message);
    }

    public TypeHandlingException(String message, Throwable cause) {
        super(message, cause);
    }
}
