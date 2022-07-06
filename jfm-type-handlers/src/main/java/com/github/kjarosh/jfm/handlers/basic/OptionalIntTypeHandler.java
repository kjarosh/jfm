package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandlerService;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Type;
import java.util.OptionalInt;

/**
 * @author Kamil Jarosz
 */
@RegisterTypeHandler
public class OptionalIntTypeHandler<T> implements TypeHandler<OptionalInt> {
    private final TypeHandlerService typeHandlerService = FilesystemMapper.instance().getTypeHandlerService();

    @Override
    public TypeReference<OptionalInt> getHandledType() {
        return new TypeReference<OptionalInt>() {
        };
    }

    @Override
    public OptionalInt read(Type actualType, InputStream input) throws IOException {
        PushbackInputStream pushbackInput = new PushbackInputStream(input);
        int read = pushbackInput.read();
        if (read == -1) {
            return OptionalInt.empty();
        }
        pushbackInput.unread(read);

        TypeHandler<Integer> originalHandler = typeHandlerService.getHandlerFor(int.class);
        return OptionalInt.of(originalHandler.read(actualType, pushbackInput));
    }

    @Override
    public void write(Type actualType, OutputStream output, OptionalInt content) throws IOException {
        TypeHandler<Integer> originalHandler = typeHandlerService.getHandlerFor(int.class);
        if (content.isPresent()) {
            originalHandler.write(actualType, output, content.getAsInt());
        }
    }

    @Override
    public byte[] serialize(Type actualType, OptionalInt content) {
        TypeHandler<Integer> originalHandler = typeHandlerService.getHandlerFor(int.class);
        if (!content.isPresent()) {
            return new byte[0];
        } else {
            return originalHandler.serialize(int.class, content.getAsInt());
        }
    }

    @Override
    public OptionalInt deserialize(Type actualType, byte[] data) {
        if (data.length == 0) {
            return OptionalInt.empty();
        } else {
            TypeHandler<Integer> innerHandler = typeHandlerService.getHandlerFor(int.class);
            return OptionalInt.of(innerHandler.deserialize(int.class, data));
        }
    }
}
