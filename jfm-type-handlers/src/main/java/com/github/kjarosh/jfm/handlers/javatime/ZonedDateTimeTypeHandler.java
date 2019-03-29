package com.github.kjarosh.jfm.handlers.javatime;

import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;

@RegisterTypeHandler
public class ZonedDateTimeTypeHandler extends AbstractByteArrayTypeHandler<ZonedDateTime> {
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
