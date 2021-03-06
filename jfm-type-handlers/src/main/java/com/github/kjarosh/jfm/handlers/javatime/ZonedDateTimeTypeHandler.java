package com.github.kjarosh.jfm.handlers.javatime;

import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;

/**
 * @author Kamil Jarosz
 */
@RegisterTypeHandler
public class ZonedDateTimeTypeHandler implements TypeHandler<ZonedDateTime> {
    @Override
    public TypeReference<ZonedDateTime> getHandledType() {
        return new TypeReference<ZonedDateTime>() {
        };
    }

    @Override
    public ZonedDateTime deserialize(Type actualType, byte[] data) {
        return ZonedDateTime.parse(new String(data, StandardCharsets.UTF_8));
    }

    @Override
    public byte[] serialize(Type actualType, ZonedDateTime content) {
        return content.toString().getBytes();
    }
}
