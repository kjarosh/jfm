package com.github.kjarosh.jfm.api.types;

import java.lang.reflect.Type;

public interface TypeHandlerService {
    <T> TypeHandler<T> getHandlerFor(Class<T> clazz);

    TypeHandler<?> getHandlerFor(Type type);

    void addHandler(Class<? extends TypeHandler> handlerClass);

    default void registerHandlersFromPackage(Package pkg) {
        registerHandlersFromPackage(pkg.getName());
    }

    void registerHandlersFromPackage(String packageName);
}
