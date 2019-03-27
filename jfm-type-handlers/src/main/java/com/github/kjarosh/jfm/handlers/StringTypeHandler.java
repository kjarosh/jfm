package com.github.kjarosh.jfm.handlers;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeReference;

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
    public String handleRead(Type actualType, byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }
}
