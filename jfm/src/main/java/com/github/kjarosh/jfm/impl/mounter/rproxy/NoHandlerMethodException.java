package com.github.kjarosh.jfm.impl.mounter.rproxy;

import com.github.kjarosh.jfm.api.FilesystemMapperException;

/**
 * @author Kamil Jarosz
 */
public class NoHandlerMethodException extends FilesystemMapperException {
    public NoHandlerMethodException(String message) {
        super(message);
    }

    public NoHandlerMethodException(String message, Throwable cause) {
        super(message, cause);
    }
}
