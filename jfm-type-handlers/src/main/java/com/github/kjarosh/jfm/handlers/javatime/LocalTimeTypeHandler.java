package com.github.kjarosh.jfm.handlers.javatime;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeReference;
import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;

@RegisterTypeHandler
public class LocalTimeTypeHandler extends AbstractByteArrayTypeHandler<LocalTime> {
    @Override
    public TypeReference<LocalTime> getHandledType() {
        return new TypeReference<LocalTime>() {
        };
    }

    @Override
    public LocalTime handleRead(Type actualType, byte[] data) {
        return LocalTime.parse(new String(data, StandardCharsets.UTF_8));
    }
}
