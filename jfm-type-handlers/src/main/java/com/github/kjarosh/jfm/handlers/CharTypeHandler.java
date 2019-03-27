package com.github.kjarosh.jfm.handlers;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeReference;

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
    public Character handleRead(Type actualType, byte[] data) {
        return new String(data, StandardCharsets.UTF_8).toCharArray()[0];
    }
}
