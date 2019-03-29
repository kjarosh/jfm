package com.github.kjarosh.jfm.handlers.listing;

import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.spi.types.ListingTypeHandler;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;
import com.github.kjarosh.jfm.spi.types.TypeReferences;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RegisterTypeHandler
public class FilesystemResourceListListingTypeHandler<T> implements ListingTypeHandler<List<T>> {
    @Override
    public TypeReference<List<T>> getHandledType() {
        return new TypeReference<List<T>>() {
        };
    }

    @Override
    public boolean isAppropriate(Type actualType) {
        Type resourceType = getResourceType(actualType);
        return resourceType instanceof Class<?> &&
                ((Class) resourceType).isAnnotationPresent(FilesystemResource.class);
    }

    @Override
    public List<T> list(Type actualType, Path path) throws IOException {
        Class<T> resourceClass = getResourceClass(actualType);

        FilesystemResourceStreamListingTypeHandler<T> handler =
                new FilesystemResourceStreamListingTypeHandler<>();

        return handler.list(resourceClass, path)
                .collect(Collectors.toList());
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
