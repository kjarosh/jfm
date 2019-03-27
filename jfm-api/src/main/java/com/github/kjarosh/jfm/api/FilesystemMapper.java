package com.github.kjarosh.jfm.api;

import com.github.kjarosh.jfm.api.types.TypeHandlerService;

import java.nio.file.Path;

public interface FilesystemMapper {
    FilesystemMapperTarget getTarget(Path path);

    TypeHandlerService getTypeHandlerService();

    static FilesystemMapper instance() {
        return FilesystemMapperProvider.getInstance();
    }
}
