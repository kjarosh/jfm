package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandlingException;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;

/**
 * @author Kamil Jarosz
 */
@RegisterTypeHandler
public class DoubleTypeHandler implements TypeHandler<Double> {
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
