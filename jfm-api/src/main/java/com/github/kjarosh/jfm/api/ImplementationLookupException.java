package com.github.kjarosh.jfm.api;

public class ImplementationLookupException extends RuntimeException {
    public ImplementationLookupException(String message) {
        super(message);
    }

    public ImplementationLookupException(String message, Throwable cause) {
        super(message, cause);
    }
}
