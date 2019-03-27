package com.github.kjarosh.jfm.handlers;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandlingException;
import com.github.kjarosh.jfm.api.types.TypeReference;

import java.lang.reflect.Type;

@RegisterTypeHandler
public class ShortTypeHandler extends AbstractByteArrayTypeHandler<Short> {
    @Override
    public TypeReference<Short> getHandledType() {
        return new TypeReference<Short>() {
        };
    }

    @Override
    public Short handleRead(Type actualType, byte[] data) {
        try {
            return Short.parseShort(new String(data));
        } catch (NumberFormatException e) {
            throw new TypeHandlingException("Wrong number format", e);
        }
    }
}
