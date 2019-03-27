package com.github.kjarosh.jfm.handlers;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeReference;

import java.lang.reflect.Type;

@RegisterTypeHandler
public class ByteTypeHandler extends AbstractByteArrayTypeHandler<Byte> {
    @Override
    public TypeReference<Byte> getHandledType() {
        return new TypeReference<Byte>() {
        };
    }

    @Override
    public Byte handleRead(Type actualType, byte[] data) {
        return data[0];
    }
}
