package com.github.kjarosh.jfm.spi.types;

public interface TypeHandlerBase<T> {
    TypeReference<T> getHandledType();
}
