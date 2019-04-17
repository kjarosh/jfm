package com.github.kjarosh.jfm.impl.mounter;

/**
 * @author Kamil Jarosz
 */
public class ErrorCodeException extends Exception {
    private int errorCode;

    public ErrorCodeException(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
