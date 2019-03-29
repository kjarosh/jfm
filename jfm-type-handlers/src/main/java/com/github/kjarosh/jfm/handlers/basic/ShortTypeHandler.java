package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandlingException;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;

@RegisterTypeHandler
public class ShortTypeHandler extends AbstractByteArrayTypeHandler<Short> {
    @Override
    public TypeReference<Short> getHandledType() {
        return new TypeReference<Short>() {
        };
    }

    @Override
    public Short deserialize(Type actualType, byte[] data) {
        try {
            return Short.parseShort(new String(data));
        } catch (NumberFormatException e) {
            throw new TypeHandlingException("Wrong number format", e);
        }
    }

    @Override
    public byte[] serialize(Type actualType, Short content) {
        return content.toString().getBytes();
    }
}
