package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandlingException;
import com.github.kjarosh.jfm.api.types.TypeReference;
import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;

import java.lang.reflect.Type;

@RegisterTypeHandler
public class BooleanTypeHandler extends AbstractByteArrayTypeHandler<Boolean> {
    private static final byte[] BYTES_TRUE = {'t', 'r', 'u', 'e'};
    private static final byte[] BYTES_FALSE = {'f', 'a', 'l', 's', 'e'};

    @Override
    public TypeReference<Boolean> getHandledType() {
        return new TypeReference<Boolean>() {
        };
    }

    @Override
    public Boolean deserialize(Type actualType, byte[] data) {
        try {
            return Boolean.parseBoolean(new String(data));
        } catch (NumberFormatException e) {
            throw new TypeHandlingException("Wrong number format", e);
        }
    }

    @Override
    public byte[] serialize(Type actualType, Boolean content) {
        return content ? BYTES_TRUE : BYTES_FALSE;
    }
}