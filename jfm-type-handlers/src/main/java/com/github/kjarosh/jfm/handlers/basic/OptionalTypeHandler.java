package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandlerService;
import com.github.kjarosh.jfm.api.types.TypeReference;
import com.github.kjarosh.jfm.api.types.TypeReferences;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Optional;

@RegisterTypeHandler
public class OptionalTypeHandler<T> implements TypeHandler<Optional<T>> {
    private TypeHandlerService typeHandlerService = FilesystemMapper.instance().getTypeHandlerService();

    @Override
    public TypeReference<Optional<T>> getHandledType() {
        return new TypeReference<Optional<T>>() {
        };
    }

    @Override
    public Optional<T> handleRead(Type actualType, Path path) throws IOException {
        if (!Files.exists(path)) {
            return Optional.empty();
        }

        Type innerType = getInnerType(actualType);

        @SuppressWarnings("unchecked")
        TypeHandler<T> innerHandler = (TypeHandler<T>) typeHandlerService.getHandlerFor(innerType);

        return Optional.of(innerHandler.handleRead(innerType, path));
    }

    @Override
    public void handleWrite(Type actualType, Path path, Optional<T> content, OpenOption[] openOptions) throws IOException {
        Type innerType = getInnerType(actualType);

        @SuppressWarnings("unchecked")
        TypeHandler<T> innerHandler = (TypeHandler<T>) typeHandlerService.getHandlerFor(innerType);

        if (!content.isPresent()) {
            Files.deleteIfExists(path);
        } else {
            innerHandler.handleWrite(actualType, path, content.get(), openOptions);
        }
    }

    private Type getInnerType(Type actualType) {
        return TypeReferences.getTypeFromVariable(getHandledType(), actualType, "T")
                .orElseThrow(AssertionError::new);
    }
}