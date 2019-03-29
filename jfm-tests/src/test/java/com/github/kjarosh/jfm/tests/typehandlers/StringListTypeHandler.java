package com.github.kjarosh.jfm.tests.typehandlers;

import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

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
    public List<String> deserialize(Type actualType, byte[] data) {
        return Arrays.asList(new String(data).split(","));
    }

    @Override
    public byte[] serialize(Type actualType, List<String> list) {
        return String.join(",", list).getBytes();
    }
}
