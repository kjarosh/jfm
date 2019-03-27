package com.github.kjarosh.jfm.impl;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.Read;
import com.github.kjarosh.jfm.api.annotations.Write;
import com.github.kjarosh.jfm.api.types.TypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandlerService;

import java.io.IOException;
import java.lang.reflect.Type;

class FilesystemMapperMethodInvoker {
    private final TypeHandlerService typeHandlerService = FilesystemMapper.instance().getTypeHandlerService();
    private final InvokeContext invokeContext;

    FilesystemMapperMethodInvoker(InvokeContext invokeContext) {
        this.invokeContext = invokeContext;
    }

    Object invokeRead(Read readAnnotation) {
        try {
            Type type = invokeContext.getReturnType();
            TypeHandler<?> returnTypeHandler = typeHandlerService.getHandlerFor(type);
            return returnTypeHandler.handleRead(type, invokeContext.getFinalPath());
        } catch (IOException e) {
            throw new FilesystemMapperException(
                    "IO Exception while invoking a method " + invokeContext.getFullName(), e);
        }
    }

    Object invokeWrite(Write writeAnnotation) {
        return null;
    }
}
