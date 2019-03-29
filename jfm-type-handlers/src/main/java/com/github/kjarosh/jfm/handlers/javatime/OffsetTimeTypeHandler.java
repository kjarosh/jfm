package com.github.kjarosh.jfm.handlers.javatime;

import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.OffsetTime;

@RegisterTypeHandler
public class OffsetTimeTypeHandler extends AbstractByteArrayTypeHandler<OffsetTime> {
    @Override
    public TypeReference<OffsetTime> getHandledType() {
        return new TypeReference<OffsetTime>() {
        };
    }

    @Override
    public OffsetTime deserialize(Type actualType, byte[] data) {
        return OffsetTime.parse(new String(data, StandardCharsets.UTF_8));
    }

    @Override
    public byte[] serialize(Type actualType, OffsetTime content) {
        return content.toString().getBytes();
    }
}
