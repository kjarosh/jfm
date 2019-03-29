package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandlingException;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;

/**
 * @author Kamil Jarosz
 */
@RegisterTypeHandler
public class FloatTypeHandler extends AbstractByteArrayTypeHandler<Float> {
    @Override
    public TypeReference<Float> getHandledType() {
        return new TypeReference<Float>() {
        };
    }

    @Override
    public Float deserialize(Type actualType, byte[] data) {
        try {
            return Float.parseFloat(new String(data));
        } catch (NumberFormatException e) {
            throw new TypeHandlingException("Wrong number format", e);
        }
    }

    @Override
    public byte[] serialize(Type actualType, Float content) {
        return content.toString().getBytes();
    }
}
