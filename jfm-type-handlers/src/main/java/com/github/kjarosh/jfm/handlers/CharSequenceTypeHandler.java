package com.github.kjarosh.jfm.handlers;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandlerProvider;
import com.github.kjarosh.jfm.api.types.TypeReference;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;

@RegisterTypeHandler
public class CharSequenceTypeHandler implements TypeHandler<CharSequence> {
    private TypeHandlerProvider typeHandlerProvider = FilesystemMapper.instance().getTypeHandlerProvider();
    private TypeHandler<String> stringHandler = typeHandlerProvider.getHandlerFor(String.class);

    @Override
    public TypeReference<CharSequence> getHandledType() {
        return new TypeReference<CharSequence>() {
        };
    }

    @Override
    public CharSequence handleRead(Type actualType, Path path) throws IOException {
        return stringHandler.handleRead(actualType, path);
    }
}
