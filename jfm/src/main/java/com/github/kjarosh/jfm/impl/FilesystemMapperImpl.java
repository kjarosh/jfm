package com.github.kjarosh.jfm.impl;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.FilesystemMapperTarget;

import java.nio.file.Path;

public class FilesystemMapperImpl implements FilesystemMapper {
    @Override
    public FilesystemMapperTarget getTarget(Path path) {
        return new FilesystemMapperTargetImpl(path);
    }
}
