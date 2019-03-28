package com.github.kjarosh.jfm.tests.typehandlers;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeReference;
import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;

import java.lang.reflect.Type;

@RegisterTypeHandler
public class TextTypeHandler extends AbstractByteArrayTypeHandler<Text> {
    @Override
    public TypeReference<Text> getHandledType() {
        return new TypeReference<Text>() {
        };
    }

    @Override
    public Text deserialize(Type actualType, byte[] data) {
        return new Text(new String(data));
    }

    @Override
    public byte[] serialize(Type actualType, Text content) {
        return content.getData().getBytes();
    }
}
