package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeReference;
import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;

import java.lang.reflect.Type;

@RegisterTypeHandler
public class ByteArrayTypeHandler extends AbstractByteArrayTypeHandler<byte[]> {
    @Override
    public TypeReference<byte[]> getHandledType() {
        return new TypeReference<byte[]>() {
        };
    }

    @Override
    public byte[] handleRead(Type actualType, byte[] data) {
        return data;
    }
}
