package com.github.kjarosh.jfm.impl;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.Read;
import com.github.kjarosh.jfm.api.annotations.Write;
import com.github.kjarosh.jfm.api.types.TypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandlerService;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

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

    @SuppressWarnings("unchecked")
    <T> void invokeWrite(Write writeAnnotation) {
        try {
            Object content = invokeContext.getContent();
            Type type = invokeContext.getContentType();
            if (type == null) {
                throw new FilesystemMapperException("No content found on " + invokeContext.getFullName());
            }

            TypeHandler<T> returnTypeHandler = (TypeHandler<T>) typeHandlerService.getHandlerFor(type);
            OpenOption[] openOptions = getOpenOptions(writeAnnotation);
            returnTypeHandler.handleWrite(type, invokeContext.getFinalPath(), (T) content, openOptions);
        } catch (IOException e) {
            throw new FilesystemMapperException(
                    "IO Exception while invoking a method " + invokeContext.getFullName(), e);
        }
    }

    private OpenOption[] getOpenOptions(Write writeAnnotation) {
        List<OpenOption> ret = new ArrayList<>();

        if (writeAnnotation.append()) {
            ret.add(StandardOpenOption.APPEND);
        }

        if (writeAnnotation.create()) {
            ret.add(StandardOpenOption.CREATE);
        }

        return ret.toArray(new OpenOption[0]);
    }
}
