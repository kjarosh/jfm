package com.github.kjarosh.jfm.impl.types;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandlerService;
import com.github.kjarosh.jfm.api.types.TypeReferences;
import com.github.kjarosh.jfm.handlers.JfmHandlers;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class TypeHandlerServiceImpl implements TypeHandlerService {
    private static final Logger logger = LoggerFactory.getLogger(TypeHandlerServiceImpl.class);

    private final ConcurrentMap<Type, TypeHandler<?>> handlers = new ConcurrentHashMap<>();

    private boolean initialized = false;

    private void initialize() {
        if (initialized) return;
        initialized = true;

        addHandlers(JfmHandlers.getJfmHandlers());
    }

    @Override
    public void addHandler(Class<? extends TypeHandler> handlerClass) {
        registerTypeHandler(instantiateHandler(handlerClass));
    }

    @Override
    public void registerHandlersFromPackage(String packageName) {
        initialize();
        addHandlers(new Reflections(packageName)
                .getSubTypesOf(TypeHandler.class)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(RegisterTypeHandler.class)));
    }

    public void addHandlers(Stream<Class<? extends TypeHandler>> handlerClasses) {
        handlerClasses.map(this::instantiateHandler)
                .forEach(this::registerTypeHandler);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeHandler<T> getHandlerFor(Class<T> clazz) {
        return (TypeHandler<T>) getHandlerFor((Type) clazz);
    }

    @Override
    public TypeHandler<?> getHandlerFor(Type type) {
        initialize();
        type = PrimitiveTypeMapper.mapPossiblePrimitive(type);

        TypeHandler<?> handler = findHandler(type);
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

    private void registerTypeHandler(TypeHandler<?> handler) {
        Type type = TypeReferences.getType(handler.getHandledType());
        if (handlers.containsKey(type)) {
            if (handlers.get(type).getClass() == handler.getClass()) {
                logger.info("The handler " + handler.getClass() + " has already been registered");
                return;
            }

            throw new FilesystemMapperException(
                    "Type handler for " + type + ": " + handler.getClass() +
                            " duplicates " + handlers.get(type).getClass());
        }
        handlers.put(type, handler);
        logger.info("Registered a handler for " + type + " (" + handler.getClass() + ")");
    }

    private <T extends TypeHandler> T instantiateHandler(Class<T> c) {
        try {
            return c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new FilesystemMapperException("Cannot instantiate a type handler", e);
        }
    }
}