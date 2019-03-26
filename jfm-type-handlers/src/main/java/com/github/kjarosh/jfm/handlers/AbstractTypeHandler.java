package com.github.kjarosh.jfm.handlers;

import com.github.kjarosh.jfm.api.handler.TypeHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractTypeHandler<T> implements TypeHandler<T> {
    @Override
    public T handleRead(Path path) throws IOException {
        return handleRead(Files.readAllBytes(path));
    }

    public abstract T handleRead(byte[] data);
}
