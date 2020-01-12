package com.github.kjarosh.jfm.impl.util;

/**
 * @author Kamil Jarosz
 */
public interface ThrowingSupplier<R, T extends Throwable> {
    R get() throws T;
}
