package com.github.kjarosh.jfm.api.handler;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import org.reflections.Reflections;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class TypeHandlers {
    private static ConcurrentMap<Class<?>, TypeHandler<?>> handlers = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> TypeHandler<T> getHandlerFor(Class<T> type) {
        TypeHandler<T> handler = (TypeHandler<T>) handlers.get(type);
        if (handler != null) {
            return handler;
        }

        discoverHandlers();
        handler = (TypeHandler<T>) handlers.get(type);

        if (handler != null) {
            return handler;
        }

        throw new FilesystemMapperException("No type handler found for: " + type);
    }

    private static void discoverHandlers() {
        Set<Class<? extends TypeHandler>> discovered = new Reflections().getSubTypesOf(TypeHandler.class);
        discovered.stream()
                .flatMap(TypeHandlers::instantiateHandler)
                .forEach(h -> handlers.put(h.getHandledClass(), h));
    }

    private static <T extends TypeHandler> Stream<T> instantiateHandler(Class<T> c) {
        try {
            return Stream.of(c.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            return Stream.empty();
        }
    }
}
