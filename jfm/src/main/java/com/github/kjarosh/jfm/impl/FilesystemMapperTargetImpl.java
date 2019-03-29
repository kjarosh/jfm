package com.github.kjarosh.jfm.impl;

import com.github.kjarosh.jfm.api.FilesystemMapperTarget;
import com.github.kjarosh.jfm.impl.mounter.FilesystemMapperMounter;
import com.github.kjarosh.jfm.impl.proxy.FilesystemMapperProxyHandler;

import java.lang.reflect.Proxy;
import java.nio.file.Path;

/**
 * @author Kamil Jarosz
 */
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

    @Override
    public void mount(Object resource) {
        new FilesystemMapperMounter(path).mount(resource);
    }
}
