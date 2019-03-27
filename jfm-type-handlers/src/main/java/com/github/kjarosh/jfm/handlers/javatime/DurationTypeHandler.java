package com.github.kjarosh.jfm.handlers.javatime;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeReference;
import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@RegisterTypeHandler
public class DurationTypeHandler extends AbstractByteArrayTypeHandler<Duration> {
    @Override
    public TypeReference<Duration> getHandledType() {
        return new TypeReference<Duration>() {
        };
    }

    @Override
    public Duration handleRead(Type actualType, byte[] data) {
        return Duration.parse(new String(data, StandardCharsets.UTF_8));
    }
}
