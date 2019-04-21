package com.github.kjarosh.jfm.handlers.javatime;

import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author Kamil Jarosz
 */
@RegisterTypeHandler
public class LocalDateTimeTypeHandler implements TypeHandler<LocalDateTime> {
    @Override
    public TypeReference<LocalDateTime> getHandledType() {
        return new TypeReference<LocalDateTime>() {
        };
    }

    @Override
    public LocalDateTime deserialize(Type actualType, byte[] data) {
        return LocalDateTime.parse(new String(data, StandardCharsets.UTF_8));
    }

    @Override
    public byte[] serialize(Type actualType, LocalDateTime content) {
        return content.toString().getBytes();
    }
}
