package com.github.kjarosh.jfm.tests.typehandlers;

import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;

@RegisterTypeHandler
public class TextTypeHandler implements TypeHandler<Text> {
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
