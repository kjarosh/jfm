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
public class IntegerTypeHandler extends AbstractByteArrayTypeHandler<Integer> {
    @Override
    public TypeReference<Integer> getHandledType() {
        return new TypeReference<Integer>() {
        };
    }

    @Override
    public Integer deserialize(Type actualType, byte[] data) {
        try {
            return Integer.parseInt(new String(data));
        } catch (NumberFormatException e) {
            throw new TypeHandlingException("Wrong number format", e);
        }
    }

    @Override
    public byte[] serialize(Type actualType, Integer content) {
        return content.toString().getBytes();
    }
}
