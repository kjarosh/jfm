package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandlerService;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.OptionalInt;

@RegisterTypeHandler
public class OptionalIntTypeHandler<T> implements TypeHandler<OptionalInt> {
    private TypeHandlerService typeHandlerService = FilesystemMapper.instance().getTypeHandlerService();

    @Override
    public TypeReference<OptionalInt> getHandledType() {
        return new TypeReference<OptionalInt>() {
        };
    }

    @Override
    public OptionalInt read(Type actualType, Path path) throws IOException {
        if (!Files.exists(path)) {
            return OptionalInt.empty();
        }

        TypeHandler<Integer> originalHandler = typeHandlerService.getHandlerFor(int.class);
        return OptionalInt.of(originalHandler.read(actualType, path));
    }

    @Override
    public void write(Type actualType, Path path, OptionalInt content) throws IOException {
        TypeHandler<Integer> originalHandler = typeHandlerService.getHandlerFor(int.class);
        if (!content.isPresent()) {
            Files.deleteIfExists(path);
        } else {
            originalHandler.write(actualType, path, content.getAsInt());
        }
    }

    @Override
    public byte[] serialize(Type actualType, OptionalInt content) {
        TypeHandler<Integer> originalHandler = typeHandlerService.getHandlerFor(int.class);
        if (!content.isPresent()) {
            return null;
        } else {
            return originalHandler.serialize(int.class, content.getAsInt());
        }
    }

    @Override
    public OptionalInt deserialize(Type actualType, byte[] data) {
        TypeHandler<Integer> originalHandler = typeHandlerService.getHandlerFor(int.class);
        if (data == null) {
            return OptionalInt.empty();
        } else {
            return OptionalInt.of(originalHandler.deserialize(int.class, data));
        }
    }
}
