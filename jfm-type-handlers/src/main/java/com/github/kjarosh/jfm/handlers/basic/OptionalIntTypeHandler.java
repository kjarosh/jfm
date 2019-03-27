package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandlerProvider;
import com.github.kjarosh.jfm.api.types.TypeReference;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.OptionalInt;

@RegisterTypeHandler
public class OptionalIntTypeHandler<T> implements TypeHandler<OptionalInt> {
    private TypeHandlerProvider typeHandlerProvider = FilesystemMapper.instance().getTypeHandlerProvider();
    private TypeHandler<Integer> originalHandler = typeHandlerProvider.getHandlerFor(int.class);

    @Override
    public TypeReference<OptionalInt> getHandledType() {
        return new TypeReference<OptionalInt>() {
        };
    }

    @Override
    public OptionalInt handleRead(Type actualType, Path path) throws IOException {
        if (!Files.exists(path)) {
            return OptionalInt.empty();
        }

        return OptionalInt.of(originalHandler.handleRead(actualType, path));
    }
}
