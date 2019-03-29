package com.github.kjarosh.jfm.handlers.javatime;

import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.Year;

@RegisterTypeHandler
public class YearTypeHandler extends AbstractByteArrayTypeHandler<Year> {
    @Override
    public TypeReference<Year> getHandledType() {
        return new TypeReference<Year>() {
        };
    }

    @Override
    public Year deserialize(Type actualType, byte[] data) {
        return Year.parse(new String(data, StandardCharsets.UTF_8));
    }

    @Override
    public byte[] serialize(Type actualType, Year content) {
        return content.toString().getBytes();
    }
}
