package com.github.kjarosh.jfm.handlers.javatime;

import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * @author Kamil Jarosz
 */
@RegisterTypeHandler
public class DurationTypeHandler implements TypeHandler<Duration> {
    @Override
    public TypeReference<Duration> getHandledType() {
        return new TypeReference<Duration>() {
        };
    }

    @Override
    public Duration deserialize(Type actualType, byte[] data) {
        return Duration.parse(new String(data, StandardCharsets.UTF_8));
    }

    @Override
    public byte[] serialize(Type actualType, Duration content) {
        return content.toString().getBytes();
    }
}
