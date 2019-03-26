package com.github.kjarosh.jfm.api;

public class FilesystemMapperException extends RuntimeException {
    public FilesystemMapperException(String message) {
        super(message);
    }

    public FilesystemMapperException(String message, Throwable cause) {
        super(message, cause);
    }
}
