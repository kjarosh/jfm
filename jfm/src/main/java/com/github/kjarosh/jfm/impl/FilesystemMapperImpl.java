package com.github.kjarosh.jfm.impl;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.FilesystemMapperTarget;
import com.github.kjarosh.jfm.impl.mounter.FilesystemMapperMounterFactory;
import com.github.kjarosh.jfm.impl.types.TypeHandlerServiceImpl;
import com.github.kjarosh.jfm.spi.types.TypeHandlerService;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kamil Jarosz
 */
public class FilesystemMapperImpl implements FilesystemMapper {
    private TypeHandlerService typeHandlerService = new TypeHandlerServiceImpl();
    private FilesystemMapperMounterFactory mounterFactory = FilesystemMapperMounterFactory.create();

    private Map<Path, FilesystemMapperTargetImpl> targets = new HashMap<>();

    @Override
    public FilesystemMapperTarget getTarget(Path path) {
        Path absolutePath = path.normalize().toAbsolutePath();
        if (targets.containsKey(absolutePath)) {
            return targets.get(absolutePath);
        }

        FilesystemMapperTargetImpl target = new FilesystemMapperTargetImpl(mounterFactory, absolutePath);
        targets.put(absolutePath, target);
        return target;
    }

    @Override
    public TypeHandlerService getTypeHandlerService() {
        return typeHandlerService;
    }
}
