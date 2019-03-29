package com.github.kjarosh.jfm.handlers.javatime;

import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * @author Kamil Jarosz
 */
@RegisterTypeHandler
public class InstantTypeHandler extends AbstractByteArrayTypeHandler<Instant> {
    @Override
    public TypeReference<Instant> getHandledType() {
        return new TypeReference<Instant>() {
        };
    }

    @Override
    public Instant deserialize(Type actualType, byte[] data) {
        return Instant.parse(new String(data, StandardCharsets.UTF_8));
    }

    @Override
    public byte[] serialize(Type actualType, Instant content) {
        return content.toString().getBytes();
    }
}
