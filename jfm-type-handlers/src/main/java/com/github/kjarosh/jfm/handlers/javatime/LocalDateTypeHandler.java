package com.github.kjarosh.jfm.handlers.javatime;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeReference;
import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@RegisterTypeHandler
public class LocalDateTypeHandler extends AbstractByteArrayTypeHandler<LocalDate> {
    @Override
    public TypeReference<LocalDate> getHandledType() {
        return new TypeReference<LocalDate>() {
        };
    }

    @Override
    public LocalDate deserialize(Type actualType, byte[] data) {
        return LocalDate.parse(new String(data, StandardCharsets.UTF_8));
    }

    @Override
    public byte[] serialize(Type actualType, LocalDate content) {
        return content.toString().getBytes();
    }
}
