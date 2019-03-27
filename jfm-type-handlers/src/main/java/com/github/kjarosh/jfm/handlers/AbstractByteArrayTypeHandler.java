package com.github.kjarosh.jfm.handlers;

import com.github.kjarosh.jfm.api.types.TypeHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractByteArrayTypeHandler<T> implements TypeHandler<T> {
    @Override
    public T handleRead(Type actualType, Path path) throws IOException {
        return handleRead(actualType, Files.readAllBytes(path));
    }

    public abstract T handleRead(Type actualType, byte[] data);
}
