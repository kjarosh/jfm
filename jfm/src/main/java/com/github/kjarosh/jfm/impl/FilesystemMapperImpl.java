package com.github.kjarosh.jfm.impl;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.FilesystemMapperTarget;
import com.github.kjarosh.jfm.impl.types.TypeHandlerServiceImpl;
import com.github.kjarosh.jfm.spi.types.TypeHandlerService;

import java.nio.file.Path;

/**
 * @author Kamil Jarosz
 */
public class FilesystemMapperImpl implements FilesystemMapper {
    private TypeHandlerService typeHandlerService = new TypeHandlerServiceImpl();

    @Override
    public FilesystemMapperTarget getTarget(Path path) {
        return new FilesystemMapperTargetImpl(path);
    }

    @Override
    public TypeHandlerService getTypeHandlerService() {
        return typeHandlerService;
    }
}
