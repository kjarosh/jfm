package com.github.kjarosh.jfm.handlers.javatime;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeReference;
import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;

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
    public OffsetTime handleRead(Type actualType, byte[] data) {
        return OffsetTime.parse(new String(data, StandardCharsets.UTF_8));
    }
}
