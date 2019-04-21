package com.github.kjarosh.jfm.spi.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * @author Kamil Jarosz
 */
public interface TypeHandler<T> extends TypeHandlerBase<T> {
    T read(Type actualType, InputStream input) throws IOException;

    void write(Type actualType, OutputStream output, T content) throws IOException;

    byte[] serialize(Type actualType, T content);

    T deserialize(Type actualType, byte[] data);
}
