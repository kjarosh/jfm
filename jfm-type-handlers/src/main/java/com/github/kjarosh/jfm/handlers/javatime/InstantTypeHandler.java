package com.github.kjarosh.jfm.handlers.javatime;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeReference;
import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@RegisterTypeHandler
public class InstantTypeHandler extends AbstractByteArrayTypeHandler<Instant> {
    @Override
    public TypeReference<Instant> getHandledType() {
        return new TypeReference<Instant>() {
        };
    }

    @Override
    public Instant handleRead(Type actualType, byte[] data) {
        return Instant.parse(new String(data, StandardCharsets.UTF_8));
    }
}
