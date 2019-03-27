package com.github.kjarosh.jfm.api;

import com.github.kjarosh.jfm.api.types.TypeHandlerProvider;

import java.nio.file.Path;

public interface FilesystemMapper {
    FilesystemMapperTarget getTarget(Path path);

    TypeHandlerProvider getTypeHandlerProvider();

    static FilesystemMapper instance() {
        return FilesystemMapperProvider.getInstance();
    }
}
