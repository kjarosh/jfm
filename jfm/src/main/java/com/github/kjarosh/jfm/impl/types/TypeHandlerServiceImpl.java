package com.github.kjarosh.jfm.impl.types;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.handlers.JfmHandlers;
import com.github.kjarosh.jfm.spi.types.ListingTypeHandler;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandlerBase;
import com.github.kjarosh.jfm.spi.types.TypeHandlerService;
import com.github.kjarosh.jfm.spi.types.TypeReferences;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Kamil Jarosz
 */
public class TypeHandlerServiceImpl implements TypeHandlerService {
    private static final Logger logger = LoggerFactory.getLogger(TypeHandlerServiceImpl.class);

    private final ConcurrentMap<Type, TypeHandler<?>> handlers = new ConcurrentHashMap<>();
    private final ConcurrentMap<Type, ListingTypeHandler<?>> listingHandlers = new ConcurrentHashMap<>();

    private volatile boolean initialized = false;
    private volatile boolean initializing = false;
    private volatile boolean initializeFailed = false;

    private void initialize(boolean allowConcurrent) {
        if (initialized) return;
        if (initializeFailed) throw new IllegalStateException("Initialize failed");

        if (initializing) {
            if (!allowConcurrent) {
                throw new IllegalStateException("Already initializing");
            } else {
                return;
            }
        }

        initializing = true;

        try {
            addHandlers(JfmHandlers.getAllTypeHandlers());
            addListingHandlers(JfmHandlers.getAllListingTypeHandlers());
        } catch (Exception e) {
            logger.error("Exception while initializing", e);
            initializeFailed = true;
        } finally {
            initializing = false;
        }

        initialized = true;
    }

    @Override
    public void addHandler(Class<? extends TypeHandler> handlerClass) {
        registerTypeHandler(instantiateHandler(handlerClass), handlers);
    }

    @Override
    public void registerHandlersFromPackage(String packageName) {
        initialize(true);
        Reflections reflections = new Reflections(packageName);
        Predicate<AnnotatedElement> filter = clazz -> clazz.isAnnotationPresent(RegisterTypeHandler.class);
        addHandlers(reflections
                .getSubTypesOf(TypeHandler.class)
                .stream().filter(filter));
        addListingHandlers(reflections
                .getSubTypesOf(ListingTypeHandler.class)
                .stream().filter(filter));
    }

    private void addHandlers(Stream<Class<? extends TypeHandler>> handlerClasses) {
        handlerClasses.map(this::instantiateHandler)
                .forEach(handler -> registerTypeHandler(handler, handlers));
    }

    private void addListingHandlers(Stream<Class<? extends ListingTypeHandler>> handlerClasses) {
        handlerClasses.map(this::instantiateHandler)
                .forEach(handler -> registerTypeHandler(handler, listingHandlers));
    }

    private <T> T instantiateHandler(Class<T> c) {
        try {
            return c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new FilesystemMapperException("Cannot instantiate a type handler", e);
        }
    }

    private <HandlerType extends TypeHandlerBase<?>> void registerTypeHandler(
            HandlerType handler, ConcurrentMap<Type, HandlerType> handlers) {
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

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeHandler<T> getHandlerFor(Class<T> clazz) {
        return (TypeHandler<T>) getHandlerFor((Type) clazz);
    }

    @Override
    public TypeHandler<?> getHandlerFor(Type type) {
        initialize(false);
        Type mappedType = PrimitiveTypeMapper.mapPossiblePrimitive(type);

        TypeHandler<?> handler = findHandler(mappedType, handlers);
        if (handler != null) {
            return handler;
        }

        throw new FilesystemMapperException("No type handler found for: " + mappedType);
    }

    @Override
    public ListingTypeHandler<?> getListingHandlerFor(Type type) {
        initialize(false);
        Type mappedType = PrimitiveTypeMapper.mapPossiblePrimitive(type);

        ListingTypeHandler<?> handler = findHandler(mappedType, listingHandlers);
        if (handler != null) {
            return handler;
        }

        throw new FilesystemMapperException("No listing type handler found for: " + mappedType);
    }

    private <HandlerType> HandlerType findHandler(Type type, ConcurrentMap<Type, HandlerType> handlers) {
        Objects.requireNonNull(type, "Type is null");
        if (handlers.isEmpty()) return null;

        HandlerType found = handlers.get(type);
        if (found != null) {
            return found;
        }

        // now, there may be a handler for a type with type variables
        Map<Integer, List<HandlerType>> handlersBySimilarity = new HashMap<>();
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
}
