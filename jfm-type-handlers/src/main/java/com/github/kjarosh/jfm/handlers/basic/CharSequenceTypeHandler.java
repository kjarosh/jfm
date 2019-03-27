package com.github.kjarosh.jfm.handlers.basic;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandlerService;
import com.github.kjarosh.jfm.api.types.TypeReference;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;

@RegisterTypeHandler
public class CharSequenceTypeHandler implements TypeHandler<CharSequence> {
    private TypeHandlerService typeHandlerService = FilesystemMapper.instance().getTypeHandlerService();

    @Override
    public TypeReference<CharSequence> getHandledType() {
        return new TypeReference<CharSequence>() {
        };
    }

    @Override
    public CharSequence handleRead(Type actualType, Path path) throws IOException {
        TypeHandler<String> stringHandler = typeHandlerService.getHandlerFor(String.class);
        return stringHandler.handleRead(actualType, path);
    }
}
