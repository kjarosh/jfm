package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandlingException;
import com.github.kjarosh.jfm.api.types.TypeReference;
import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;

import java.lang.reflect.Type;

@RegisterTypeHandler
public class DoubleTypeHandler extends AbstractByteArrayTypeHandler<Double> {
    @Override
    public TypeReference<Double> getHandledType() {
        return new TypeReference<Double>() {
        };
    }

    @Override
    public Double deserialize(Type actualType, byte[] data) {
        try {
            return Double.parseDouble(new String(data));
        } catch (NumberFormatException e) {
            throw new TypeHandlingException("Wrong number format", e);
        }
    }

    @Override
    public byte[] serialize(Type actualType, Double content) {
        return content.toString().getBytes();
    }
}
