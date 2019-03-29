package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandlerService;
import com.github.kjarosh.jfm.spi.types.TypeReference;
import com.github.kjarosh.jfm.spi.types.TypeReferences;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * @author Kamil Jarosz
 */
@RegisterTypeHandler
public class OptionalTypeHandler<T> implements TypeHandler<Optional<T>> {
    private TypeHandlerService typeHandlerService = FilesystemMapper.instance().getTypeHandlerService();

    @Override
    public TypeReference<Optional<T>> getHandledType() {
        return new TypeReference<Optional<T>>() {
        };
    }

    @Override
    public Optional<T> read(Type actualType, Path path) throws IOException {
        if (!Files.exists(path)) {
            return Optional.empty();
        }

        Type innerType = getInnerType(actualType);

        @SuppressWarnings("unchecked")
        TypeHandler<T> innerHandler = (TypeHandler<T>) typeHandlerService.getHandlerFor(innerType);

        return Optional.of(innerHandler.read(innerType, path));
    }

    @Override
    public void write(Type actualType, Path path, Optional<T> content) throws IOException {
        TypeHandler<T> innerHandler = getInnerHandler(actualType);

        if (!content.isPresent()) {
            Files.deleteIfExists(path);
        } else {
            innerHandler.write(actualType, path, content.get());
        }
    }

    @Override
    public byte[] serialize(Type actualType, Optional<T> content) {
        TypeHandler<T> innerHandler = getInnerHandler(actualType);

        return content.map(t -> innerHandler.serialize(actualType, t))
                .orElse(null);
    }

    @Override
    public Optional<T> deserialize(Type actualType, byte[] data) {
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    private TypeHandler<T> getInnerHandler(Type actualType) {
        Type innerType = getInnerType(actualType);
        return (TypeHandler<T>) typeHandlerService.getHandlerFor(innerType);
    }

    private Type getInnerType(Type actualType) {
        return TypeReferences.getTypeFromVariable(getHandledType(), actualType, "T")
                .orElseThrow(AssertionError::new);
    }
}
