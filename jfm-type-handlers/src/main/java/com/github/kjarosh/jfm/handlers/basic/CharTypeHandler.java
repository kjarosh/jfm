package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author Kamil Jarosz
 */
@RegisterTypeHandler
public class CharTypeHandler implements TypeHandler<Character> {
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
        return String.valueOf(content).getBytes(StandardCharsets.UTF_8);
    }
}
