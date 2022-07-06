package com.github.kjarosh.jfm.handlers.listing;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.spi.types.ListingTypeHandler;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;
import com.github.kjarosh.jfm.spi.types.TypeReferences;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
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
                ((Class<?>) resourceType).isAnnotationPresent(FilesystemResource.class);
    }

    @Override
    public Stream<T> list(Type actualType, Path path) throws IOException {
        Class<T> resourceClass = getResourceClass(actualType);
        return list(resourceClass, path).stream();
    }

    public List<T> list(Class<T> resourceClass, Path path) throws IOException {
        FilesystemMapper instance = FilesystemMapper.instance();
        try (Stream<Path> list = Files.list(path)) {
            return list
                    .map(instance::getTarget)
                    .map(t -> t.proxy(resourceClass))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<String> itemize(Type actualType, Stream<T> stream) {
        Class<T> resourceClass = getResourceClass(actualType);
        return reverse(resourceClass, stream);
    }

    public List<String> reverse(Class<T> resourceClass, Stream<T> stream) {
        throw new FilesystemMapperException("Cannot use this type handler in reverse proxy");
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
