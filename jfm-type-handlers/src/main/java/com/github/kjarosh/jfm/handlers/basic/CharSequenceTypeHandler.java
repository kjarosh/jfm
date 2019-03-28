package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandlerService;
import com.github.kjarosh.jfm.api.types.TypeReference;
import com.github.kjarosh.jfm.handlers.AbstractByteArrayTypeHandler;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@RegisterTypeHandler
public class CharSequenceTypeHandler extends AbstractByteArrayTypeHandler<CharSequence> {
    private TypeHandlerService typeHandlerService = FilesystemMapper.instance().getTypeHandlerService();

    @Override
    public TypeReference<CharSequence> getHandledType() {
        return new TypeReference<CharSequence>() {
        };
    }

    @Override
    public String deserialize(Type actualType, byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }

    @Override
    public byte[] serialize(Type actualType, CharSequence content) {
        return content.toString().getBytes(StandardCharsets.UTF_8);
    }
}
