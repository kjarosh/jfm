package com.github.kjarosh.jfm.impl;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.Read;
import com.github.kjarosh.jfm.api.annotations.Write;
import com.github.kjarosh.jfm.api.handler.TypeHandler;
import com.github.kjarosh.jfm.api.handler.TypeHandlers;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;

class FilesystemMapperMethodInvoker {
    private final Object proxy;
    private final Method method;
    private final Object[] args;

    private final Path finalPath;

    FilesystemMapperMethodInvoker(Path path, Object proxy, Method method, Object[] args) {
        this.proxy = proxy;
        this.method = method;
        this.args = args;

        this.finalPath = new JfmPathResolver(path).resolveMethod(method);
    }

    Object invokeRead(Read readAnnotation) {
        try {
            TypeHandler<?> returnTypeHandler = TypeHandlers.getHandlerFor(method.getReturnType());

            return returnTypeHandler.handleRead(finalPath);
            //byte[] content = Files.readAllBytes(finalPath);
        } catch (IOException e) {
            throw new FilesystemMapperException(
                    "IO Exception while invoking a method " + method.getName(), e);
        }
    }

    Object invokeWrite(Write writeAnnotation) {
        return null;
    }
}
