package com.github.kjarosh.jfm.handlers;

import com.github.kjarosh.jfm.api.types.TypeHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractByteArrayTypeHandler<T> implements TypeHandler<T> {
    @Override
    public T read(Type actualType, Path path) throws IOException {
        return deserialize(actualType, Files.readAllBytes(path));
    }

    public abstract T deserialize(Type actualType, byte[] data);

    @Override
    public void write(Type actualType, Path path, T content) throws IOException {
        byte[] toWrite = serialize(actualType, content);
        Files.write(path, toWrite);
    }

    public abstract byte[] serialize(Type actualType, T content);
}
