package com.github.kjarosh.jfm.tests.typehandlers;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeReference;
import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

@RegisterTypeHandler
public class StringListTypeHandler extends AbstractByteArrayTypeHandler<List<String>> {
    @Override
    public TypeReference<List<String>> getHandledType() {
        return new TypeReference<List<String>>() {
        };
    }

    @Override
    public List<String> handleRead(Type actualType, byte[] data) {
        return Arrays.asList(new String(data).split(","));
    }
}
