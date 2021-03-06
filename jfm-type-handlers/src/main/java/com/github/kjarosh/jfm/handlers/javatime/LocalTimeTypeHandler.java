package com.github.kjarosh.jfm.handlers.javatime;

import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;

/**
 * @author Kamil Jarosz
 */
@RegisterTypeHandler
public class LocalTimeTypeHandler implements TypeHandler<LocalTime> {
    @Override
    public TypeReference<LocalTime> getHandledType() {
        return new TypeReference<LocalTime>() {
        };
    }

    @Override
    public LocalTime deserialize(Type actualType, byte[] data) {
        return LocalTime.parse(new String(data, StandardCharsets.UTF_8));
    }

    @Override
    public byte[] serialize(Type actualType, LocalTime content) {
        return content.toString().getBytes();
    }
}
