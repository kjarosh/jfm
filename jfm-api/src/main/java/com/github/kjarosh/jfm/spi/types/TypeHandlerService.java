package com.github.kjarosh.jfm.spi.types;

import java.lang.reflect.Type;

/**
 * @author Kamil Jarosz
 */
public interface TypeHandlerService {
    <T> TypeHandler<T> getHandlerFor(Class<T> clazz);

    TypeHandler<?> getHandlerFor(Type type);

    ListingTypeHandler<?> getListingHandlerFor(Type type);

    @SuppressWarnings("rawtypes")
    void addHandler(Class<? extends TypeHandler> handlerClass);

    default void registerHandlersFromPackage(Package pkg) {
        registerHandlersFromPackage(pkg.getName());
    }

    void registerHandlersFromPackage(String packageName);
}
