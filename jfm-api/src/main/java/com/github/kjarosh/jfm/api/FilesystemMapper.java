package com.github.kjarosh.jfm.api;

import java.nio.file.Path;

public interface FilesystemMapper {
    FilesystemMapperTarget getTarget(Path path);

    static FilesystemMapper instance() {
        return FilesystemMapperProvider.getInstance();
    }
}
