package com.github.kjarosh.jfm.handlers.javatime;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeReference;
import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@RegisterTypeHandler
public class LocalDateTimeTypeHandler extends AbstractByteArrayTypeHandler<LocalDateTime> {
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
