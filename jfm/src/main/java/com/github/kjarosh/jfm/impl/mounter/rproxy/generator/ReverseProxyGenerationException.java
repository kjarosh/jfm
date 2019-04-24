package com.github.kjarosh.jfm.impl.mounter.rproxy.generator;

/**
 * @author Kamil Jarosz
 */
public class ReverseProxyGenerationException extends RuntimeException {
    public ReverseProxyGenerationException() {
        super();
    }

    public ReverseProxyGenerationException(String message) {
        super(message);
    }

    public ReverseProxyGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReverseProxyGenerationException(Throwable cause) {
        super(cause);
    }
}
