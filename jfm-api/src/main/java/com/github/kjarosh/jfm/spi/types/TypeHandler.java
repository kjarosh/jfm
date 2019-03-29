package com.github.kjarosh.jfm.spi.types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;

public interface TypeHandler<T> extends TypeHandlerBase<T> {
    T read(Type actualType, Path path) throws IOException;

    void write(Type actualType, Path path, T content) throws IOException;

    byte[] serialize(Type actualType, T content);

    T deserialize(Type actualType, byte[] data);
}
