package com.github.kjarosh.jfm.spi.types;

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * @author Kamil Jarosz
 */
public interface TypeHandler<T> extends TypeHandlerBase<T> {
    default T read(Type actualType, InputStream input) throws IOException {
        return deserialize(actualType, ByteStreams.toByteArray(input));
    }

    default void write(Type actualType, OutputStream output, T content) throws IOException {
        output.write(serialize(actualType, content));
    }

    byte[] serialize(Type actualType, T content);

    T deserialize(Type actualType, byte[] data);
}
