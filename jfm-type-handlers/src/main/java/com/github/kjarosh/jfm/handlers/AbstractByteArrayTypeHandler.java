package com.github.kjarosh.jfm.handlers;

import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * @author Kamil Jarosz
 */
public abstract class AbstractByteArrayTypeHandler<T> implements TypeHandler<T> {
    @Override
    public T read(Type actualType, InputStream input) throws IOException {
        return deserialize(actualType, ByteStreams.toByteArray(input));
    }

    public abstract T deserialize(Type actualType, byte[] data);

    @Override
    public void write(Type actualType, OutputStream output, T content) throws IOException {
        byte[] toWrite = serialize(actualType, content);
        output.write(toWrite);
    }

    public abstract byte[] serialize(Type actualType, T content);
}
