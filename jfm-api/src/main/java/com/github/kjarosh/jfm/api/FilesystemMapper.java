package com.github.kjarosh.jfm.api;

import com.github.kjarosh.jfm.spi.types.TypeHandlerService;

import java.nio.file.Path;

/**
 * @author Kamil Jarosz
 */
public interface FilesystemMapper {
    FilesystemMapperTarget getTarget(Path path);

    TypeHandlerService getTypeHandlerService();

    static FilesystemMapper instance() {
        return FilesystemMapperProvider.getInstance();
    }
}
