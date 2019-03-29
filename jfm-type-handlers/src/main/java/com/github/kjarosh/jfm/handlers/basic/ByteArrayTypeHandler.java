package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;

@RegisterTypeHandler
public class ByteArrayTypeHandler extends AbstractByteArrayTypeHandler<byte[]> {
    @Override
    public TypeReference<byte[]> getHandledType() {
        return new TypeReference<byte[]>() {
        };
    }

    @Override
    public byte[] deserialize(Type actualType, byte[] data) {
        return data;
    }

    @Override
    public byte[] serialize(Type actualType, byte[] content) {
        return content;
    }
}
