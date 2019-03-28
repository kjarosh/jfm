package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeReference;
import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@RegisterTypeHandler
public class CharArrayTypeHandler extends AbstractByteArrayTypeHandler<char[]> {
    @Override
    public TypeReference<char[]> getHandledType() {
        return new TypeReference<char[]>() {
        };
    }

    @Override
    public char[] deserialize(Type actualType, byte[] data) {
        return new String(data, StandardCharsets.UTF_8).toCharArray();
    }

    @Override
    public byte[] serialize(Type actualType, char[] content) {
        return new String(content).getBytes(StandardCharsets.UTF_8);
    }
}
