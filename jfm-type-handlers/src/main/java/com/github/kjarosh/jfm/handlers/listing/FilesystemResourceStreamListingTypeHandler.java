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
import java.util.stream.Stream;

/**
 * @author Kamil Jarosz
 */
@RegisterTypeHandler
public class FilesystemResourceStreamListingTypeHandler<T> implements ListingTypeHandler<Stream<T>> {
    @Override
    public TypeReference<Stream<T>> getHandledType() {
        return new TypeReference<Stream<T>>() {
        };
    }

    @Override
    public boolean isAppropriate(Type actualType) {
        Type resourceType = getResourceType(actualType);
        return resourceType instanceof Class<?> &&
                ((Class) resourceType).isAnnotationPresent(FilesystemResource.class);
    }

    @Override
    public Stream<T> list(Type actualType, Path path) throws IOException {
        Class<T> resourceClass = getResourceClass(actualType);
        return list(resourceClass, path);
    }

    public Stream<T> list(Class<T> resourceClass, Path path) throws IOException {
        FilesystemMapper instance = FilesystemMapper.instance();
        return Files.list(path)
                .map(instance::getTarget)
                .map(t -> t.proxy(resourceClass));
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
