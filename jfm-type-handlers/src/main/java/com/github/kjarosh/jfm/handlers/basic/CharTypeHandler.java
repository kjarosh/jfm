package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@RegisterTypeHandler
public class CharTypeHandler extends AbstractByteArrayTypeHandler<Character> {
    @Override
    public TypeReference<Character> getHandledType() {
        return new TypeReference<Character>() {
        };
    }

    @Override
    public Character deserialize(Type actualType, byte[] data) {
        return new String(data, StandardCharsets.UTF_8).toCharArray()[0];
    }

    @Override
    public byte[] serialize(Type actualType, Character content) {
        return new String(new char[]{content}).getBytes(StandardCharsets.UTF_8);
    }
}
