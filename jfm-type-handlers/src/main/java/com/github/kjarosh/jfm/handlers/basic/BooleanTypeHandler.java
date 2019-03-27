package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandlingException;
import com.github.kjarosh.jfm.api.types.TypeReference;
import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;

import java.lang.reflect.Type;

@RegisterTypeHandler
public class BooleanTypeHandler extends AbstractByteArrayTypeHandler<Boolean> {
    @Override
    public TypeReference<Boolean> getHandledType() {
        return new TypeReference<Boolean>() {
        };
    }

    @Override
    public Boolean handleRead(Type actualType, byte[] data) {
        try {
            return Boolean.parseBoolean(new String(data));
        } catch (NumberFormatException e) {
            throw new TypeHandlingException("Wrong number format", e);
        }
    }
}
