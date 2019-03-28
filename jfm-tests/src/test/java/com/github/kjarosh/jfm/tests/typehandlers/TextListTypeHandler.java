package com.github.kjarosh.jfm.tests.typehandlers;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeReference;
import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RegisterTypeHandler
public class TextListTypeHandler extends AbstractByteArrayTypeHandler<List<Text>> {
    @Override
    public TypeReference<List<Text>> getHandledType() {
        return new TypeReference<List<Text>>() {
        };
    }

    @Override
    public List<Text> deserialize(Type actualType, byte[] data) {
        return Arrays.stream(new String(data).split(","))
                .map(Text::new)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] serialize(Type actualType, List<Text> list) {
        return list.stream()
                .map(Text::getData)
                .collect(Collectors.joining(","))
                .getBytes();
    }
}