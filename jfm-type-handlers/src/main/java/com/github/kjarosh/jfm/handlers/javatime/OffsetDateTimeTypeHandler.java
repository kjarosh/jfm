package com.github.kjarosh.jfm.handlers.javatime;

import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

/**
 * @author Kamil Jarosz
 */
@RegisterTypeHandler
public class OffsetDateTimeTypeHandler implements TypeHandler<OffsetDateTime> {
    @Override
    public TypeReference<OffsetDateTime> getHandledType() {
        return new TypeReference<OffsetDateTime>() {
        };
    }

    @Override
    public OffsetDateTime deserialize(Type actualType, byte[] data) {
        return OffsetDateTime.parse(new String(data, StandardCharsets.UTF_8));
    }

    @Override
    public byte[] serialize(Type actualType, OffsetDateTime content) {
        return content.toString().getBytes();
    }
}
