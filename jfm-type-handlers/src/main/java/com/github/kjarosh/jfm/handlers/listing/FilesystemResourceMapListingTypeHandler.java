package com.github.kjarosh.jfm.handlers.listing;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.spi.types.ListingTypeHandler;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;
import com.github.kjarosh.jfm.spi.types.TypeReferences;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Kamil Jarosz
 */
@RegisterTypeHandler
public class FilesystemResourceMapListingTypeHandler<T> implements ListingTypeHandler<Map<String, T>> {
    @Override
    public TypeReference<Map<String, T>> getHandledType() {
        return new TypeReference<Map<String, T>>() {
        };
    }

    @Override
    public boolean isAppropriate(Type actualType) {
        Type resourceType = getResourceType(actualType);
        return resourceType instanceof Class<?> &&
                ((Class<?>) resourceType).isAnnotationPresent(FilesystemResource.class);
    }

    @Override
    public Map<String, T> list(Type actualType, Path path) throws IOException {
        Class<T> resourceClass = getResourceClass(actualType);
        FilesystemMapper instance = FilesystemMapper.instance();
        return Files.list(path).collect(Collectors.toMap(
                p -> p.getFileName().toString(),
                p -> instance.getTarget(p).proxy(resourceClass),
                (a, b) -> {
                    throw new AssertionError("Duplicate filename while listing " + path);
                },
                LinkedHashMap::new));
    }

    @Override
    public List<String> itemize(Type actualType, Map<String, T> map) {
        return new ArrayList<>(map.keySet());
    }

    @SuppressWarnings("unchecked")
    private Class<T> getResourceClass(Type actualType) {
        return (Class<T>) getResourceType(actualType);
    }

    private Type getResourceType(Type actualType) {
        return TypeReferences.getTypeFromVariable(TypeReferences.getType(getHandledType()), actualType, "T")
                .orElseThrow(AssertionError::new);
    }
}
