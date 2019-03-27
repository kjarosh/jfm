package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandlingException;
import com.github.kjarosh.jfm.api.types.TypeReference;
import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;

import java.lang.reflect.Type;

@RegisterTypeHandler
public class FloatTypeHandler extends AbstractByteArrayTypeHandler<Float> {
    @Override
    public TypeReference<Float> getHandledType() {
        return new TypeReference<Float>() {
        };
    }

    @Override
    public Float handleRead(Type actualType, byte[] data) {
        try {
            return Float.parseFloat(new String(data));
        } catch (NumberFormatException e) {
            throw new TypeHandlingException("Wrong number format", e);
        }
    }
}
