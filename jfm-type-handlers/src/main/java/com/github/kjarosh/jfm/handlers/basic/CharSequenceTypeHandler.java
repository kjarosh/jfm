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
public class CharSequenceTypeHandler implements TypeHandler<CharSequence> {
    @Override
    public TypeReference<CharSequence> getHandledType() {
        return new TypeReference<CharSequence>() {
        };
    }

    @Override
    public String deserialize(Type actualType, byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }

    @Override
    public byte[] serialize(Type actualType, CharSequence content) {
        return content.toString().getBytes(StandardCharsets.UTF_8);
    }
}
