package com.github.kjarosh.jfm.spi.types;

/**
 * @author Kamil Jarosz
 */
public interface TypeHandlerBase<T> {
    TypeReference<T> getHandledType();
}
