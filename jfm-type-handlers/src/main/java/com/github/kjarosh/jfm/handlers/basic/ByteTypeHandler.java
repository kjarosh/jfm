package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;

@RegisterTypeHandler
public class ByteTypeHandler extends AbstractByteArrayTypeHandler<Byte> {
    @Override
    public TypeReference<Byte> getHandledType() {
        return new TypeReference<Byte>() {
        };
    }

    @Override
    public Byte deserialize(Type actualType, byte[] data) {
        return data[0];
    }

    @Override
    public byte[] serialize(Type actualType, Byte content) {
        return new byte[]{content};
    }
}
