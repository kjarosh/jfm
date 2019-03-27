package com.github.kjarosh.jfm.impl.types;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandlerProvider;
import com.github.kjarosh.jfm.api.types.TypeReferences;
import org.reflections.Reflections;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class TypeHandlerProviderImpl implements TypeHandlerProvider {
    private final ConcurrentMap<Type, TypeHandler<?>> handlers = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeHandler<T> getHandlerFor(Class<T> clazz) {
        return (TypeHandler<T>) getHandlerFor((Type) clazz);
    }

    @Override
    public TypeHandler<?> getHandlerFor(Type type) {
        type = PrimitiveTypeMapper.mapPossiblePrimitive(type);

        TypeHandler<?> handler = findHandler(type);
        if (handler != null) {
            return handler;
        }

        discoverHandlers();
        handler = findHandler(type);

        if (handler != null) {
            return handler;
        }

        throw new FilesystemMapperException("No type handler found for: " + type);
    }

    @SuppressWarnings("unchecked")
    private <T> TypeHandler<T> findHandler(Type type) {
        Objects.requireNonNull(type, "Type is null");
        if (handlers.isEmpty()) return null;

        TypeHandler<T> found = (TypeHandler<T>) handlers.get(type);
        if (found != null) {
            return found;
        }

        // now, there may be a handler for a type with type variables
        Map<Integer, List<TypeHandler>> handlersBySimilarity = new HashMap<>();
        handlers.forEach((handlerType, handler) -> {
            TypeReferences.calculateSimilarity(type, handlerType)
                    .ifPresent(integer -> handlersBySimilarity
                            .computeIfAbsent(integer, k -> new ArrayList<>()).add(handler));
        });

        int bestSimilarity = handlersBySimilarity.keySet()
                .stream().mapToInt(i -> i).max()
                .orElse(-1);

        if (bestSimilarity < 0) {
            return null;
        }

        int matchedHandlers = handlersBySimilarity.get(bestSimilarity).size();
        if (matchedHandlers == 0) {
            return null;
        }

        if (matchedHandlers > 1) {
            throw new FilesystemMapperException("Type handler ambiguous for: " + type);
        }

        return handlersBySimilarity.get(bestSimilarity).get(0);
    }

    @SuppressWarnings("unchecked")
    private void discoverHandlers() {
        Set<Class<?>> discovered = new Reflections()
                .getTypesAnnotatedWith(RegisterTypeHandler.class);
        discovered.stream()
                .filter(TypeHandler.class::isAssignableFrom)
                .map(c -> (Class<TypeHandler<?>>) c)
                .flatMap(this::instantiateHandler)
                .forEach(this::registerTypeHandler);
    }

    private void registerTypeHandler(TypeHandler<?> handler) {
        Type type = TypeReferences.getType(handler.getHandledType());
        if (handlers.containsKey(type)) {
            if (handlers.get(type).getClass() == handler.getClass()) {
                // those are the same handlers
                return;
            }

            throw new FilesystemMapperException(
                    "Type handler for " + type + ": " + handler.getClass() +
                            " duplicates " + handlers.get(type).getClass());
        }
        handlers.put(type, handler);
    }

    private <T extends TypeHandler> Stream<T> instantiateHandler(Class<T> c) {
        try {
            return Stream.of(c.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            return Stream.empty();
        }
    }
}
