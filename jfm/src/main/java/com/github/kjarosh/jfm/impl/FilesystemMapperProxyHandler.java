package com.github.kjarosh.jfm.impl;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Read;
import com.github.kjarosh.jfm.api.annotations.Write;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.file.Path;

public class FilesystemMapperProxyHandler<T> implements InvocationHandler {
    private final Class<T> resourceClass;
    private final Path path;

    FilesystemMapperProxyHandler(Class<T> resourceClass, Path path) {
        this.resourceClass = resourceClass;
        this.path = path;

        FilesystemResource resourceAnnotation = resourceClass
                .getAnnotation(FilesystemResource.class);

        if (resourceAnnotation == null) {
            throw new FilesystemMapperException("Resource class is not annotated with " +
                    "@" + FilesystemResource.class.getSimpleName());
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        InvokeContext ic = new InvokeContext(path, method, args);

        FilesystemMapperMethodInvoker invoker = new FilesystemMapperMethodInvoker(ic);

        Read read = method.getAnnotation(Read.class);
        Write write = method.getAnnotation(Write.class);

        if (read != null && write != null) {
            throw new FilesystemMapperException(
                    "Method " + method.getName() + " is not properly annotated");
        }

        if (read != null) {
            return invoker.invokeRead(read);
        }

        if (write != null) {
            return invoker.invokeWrite(write);
        }

        throw new FilesystemMapperException(
                "Method " + method.getName() + " is not annotated with " +
                        "@" + Read.class.getSimpleName() + " or " +
                        "@" + Write.class.getSimpleName());
    }
}
