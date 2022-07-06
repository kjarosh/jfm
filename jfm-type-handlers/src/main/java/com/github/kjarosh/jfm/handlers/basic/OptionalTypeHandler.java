package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandlerService;
import com.github.kjarosh.jfm.spi.types.TypeReference;
import com.github.kjarosh.jfm.spi.types.TypeReferences;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * @author Kamil Jarosz
 */
@RegisterTypeHandler
public class OptionalTypeHandler<T> implements TypeHandler<Optional<T>> {
    private final TypeHandlerService typeHandlerService = FilesystemMapper.instance().getTypeHandlerService();

    @Override
    public TypeReference<Optional<T>> getHandledType() {
        return new TypeReference<Optional<T>>() {
        };
    }

    @Override
    public Optional<T> read(Type actualType, InputStream input) throws IOException {
        PushbackInputStream pushbackInput = new PushbackInputStream(input);
        int read = pushbackInput.read();
        if (read == -1) {
            return Optional.empty();
        }
        pushbackInput.unread(read);

        Type innerType = getInnerType(actualType);

        @SuppressWarnings("unchecked")
        TypeHandler<T> innerHandler = (TypeHandler<T>) typeHandlerService.getHandlerFor(innerType);

        return Optional.of(innerHandler.read(innerType, pushbackInput));
    }

    @Override
    public void write(Type actualType, OutputStream output, Optional<T> content) throws IOException {
        TypeHandler<T> innerHandler = getInnerHandler(actualType);

        if (content.isPresent()) {
            innerHandler.write(actualType, output, content.get());
        }
    }

    @Override
    public byte[] serialize(Type actualType, Optional<T> content) {
        TypeHandler<T> innerHandler = getInnerHandler(actualType);

        return content.map(t -> innerHandler.serialize(actualType, t))
                .orElse(new byte[0]);
    }

    @Override
    public Optional<T> deserialize(Type actualType, byte[] data) {
        if (data.length == 0) {
            return Optional.empty();
        } else {
            TypeHandler<T> innerHandler = getInnerHandler(actualType);
            return Optional.of(innerHandler.deserialize(int.class, data));
        }
    }

    @SuppressWarnings("unchecked")
    private TypeHandler<T> getInnerHandler(Type actualType) {
        Type innerType = getInnerType(actualType);
        return (TypeHandler<T>) typeHandlerService.getHandlerFor(innerType);
    }

    private Type getInnerType(Type actualType) {
        return TypeReferences.getTypeFromVariable(getHandledType(), actualType, "T")
                .orElseThrow(AssertionError::new);
    }
}
