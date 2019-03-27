package com.github.kjarosh.jfm.api.types;

import java.lang.reflect.Type;

public interface TypeHandlerProvider {
    <T> TypeHandler<T> getHandlerFor(Class<T> clazz);

    TypeHandler<?> getHandlerFor(Type type);
}
