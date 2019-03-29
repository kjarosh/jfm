package com.github.kjarosh.jfm.spi.types;

/**
 * @author Kamil Jarosz
 */
public class TypeHandlingException extends RuntimeException {
    public TypeHandlingException(String message) {
        super(message);
    }

    public TypeHandlingException(String message, Throwable cause) {
        super(message, cause);
    }
}
