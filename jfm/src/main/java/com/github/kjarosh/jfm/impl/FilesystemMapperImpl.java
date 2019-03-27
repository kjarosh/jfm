package com.github.kjarosh.jfm.impl;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.FilesystemMapperTarget;
import com.github.kjarosh.jfm.api.types.TypeHandlerProvider;
import com.github.kjarosh.jfm.impl.types.TypeHandlerProviderImpl;

import java.nio.file.Path;

public class FilesystemMapperImpl implements FilesystemMapper {
    private TypeHandlerProvider typeHandlerProvider = new TypeHandlerProviderImpl();

    @Override
    public FilesystemMapperTarget getTarget(Path path) {
        return new FilesystemMapperTargetImpl(path);
    }

    @Override
    public TypeHandlerProvider getTypeHandlerProvider() {
        return typeHandlerProvider;
    }
}
