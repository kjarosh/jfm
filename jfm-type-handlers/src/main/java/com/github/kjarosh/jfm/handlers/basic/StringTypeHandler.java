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
public class StringTypeHandler implements TypeHandler<String> {
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
