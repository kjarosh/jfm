package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@RegisterTypeHandler
public class StringTypeHandler extends AbstractByteArrayTypeHandler<String> {
    @Override
    public TypeReference<String> getHandledType() {
        return new TypeReference<String>() {
        };
    }

    @Override
    public String deserialize(Type actualType, byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }

    @Override
    public byte[] serialize(Type actualType, String content) {
        return content.getBytes();
    }
}
