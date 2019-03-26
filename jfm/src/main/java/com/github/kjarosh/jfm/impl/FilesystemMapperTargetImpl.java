package com.github.kjarosh.jfm.impl;

import com.github.kjarosh.jfm.api.FilesystemMapperTarget;

import java.lang.reflect.Proxy;
import java.nio.file.Path;

public class FilesystemMapperTargetImpl implements FilesystemMapperTarget {
    private Path path;

    FilesystemMapperTargetImpl(Path path) {
        this.path = path;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T proxy(Class<T> resourceClass) {
        return (T) Proxy.newProxyInstance(resourceClass.getClassLoader(),
                new Class[]{resourceClass},
                new FilesystemMapperProxyHandler(resourceClass, path));
    }
}
