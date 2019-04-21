package com.github.kjarosh.jfm.impl.proxy;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.*;
import com.github.kjarosh.jfm.impl.MethodHandler;
import com.github.kjarosh.jfm.spi.types.ListingTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandlerService;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Kamil Jarosz
 */
class ResourceMethodProxy implements MethodHandler<Object> {
    private final TypeHandlerService typeHandlerService = FilesystemMapper.instance().getTypeHandlerService();
    private final InvokeContext invokeContext;

    ResourceMethodProxy(InvokeContext invokeContext) {
        this.invokeContext = invokeContext;
    }

    private FilesystemMapperException newJFMException(IOException e) {
        return new FilesystemMapperException(
                "IO Exception while invoking a method " + invokeContext.getFullName(), e);
    }

    public Object handleRead(Read readAnnotation) {
        try {
            Type type = invokeContext.getReturnType();
            TypeHandler<?> returnTypeHandler = typeHandlerService.getHandlerFor(type);
            Path path = invokeContext.getFinalPath();
            return returnTypeHandler.read(type, Files.newInputStream(path));
        } catch (IOException e) {
            throw newJFMException(e);
        }
    }

    @Override
    public Object handleWrite(Write writeAnnotation) {
        return handleWrite0(writeAnnotation);
    }

    @SuppressWarnings("unchecked")
    private <T> Object handleWrite0(Write writeAnnotation) {
        try {
            Object content = invokeContext.getContent();
            Type type = invokeContext.getContentType();
            if (type == null) {
                throw new FilesystemMapperException("No content found on " + invokeContext.getFullName());
            }

            TypeHandler<T> returnTypeHandler = (TypeHandler<T>) typeHandlerService.getHandlerFor(type);
            Path path = invokeContext.getFinalPath();
            returnTypeHandler.write(type, Files.newOutputStream(path), (T) content);
            return null;
        } catch (IOException e) {
            throw newJFMException(e);
        }
    }

    private <T> Object writeConstantValue(Class<T> type, T value) throws IOException {
        TypeHandler<T> returnTypeHandler = typeHandlerService.getHandlerFor(type);
        Path path = invokeContext.getFinalPath();
        returnTypeHandler.write(type, Files.newOutputStream(path), value);
        return null;
    }

    @Override
    public Object handleWriteBytes(WriteBytes writeBytes) {
        try {
            return writeConstantValue(byte[].class, writeBytes.value());
        } catch (IOException e) {
            throw newJFMException(e);
        }
    }

    @Override
    public Object handleWriteString(WriteString writeString) {
        try {
            return writeConstantValue(String.class, writeString.value());
        } catch (IOException e) {
            throw newJFMException(e);
        }
    }

    @Override
    public Object handleWriteBoolean(WriteBoolean writeBoolean) {
        try {
            return writeConstantValue(boolean.class, writeBoolean.value());
        } catch (IOException e) {
            throw newJFMException(e);
        }
    }

    @Override
    public Object handleWriteInteger(WriteInteger writeInteger) {
        try {
            return writeConstantValue(int.class, writeInteger.value());
        } catch (IOException e) {
            throw newJFMException(e);
        }
    }

    @Override
    public Object handleDelete(Delete delete) {
        try {
            if (delete.failIfNotExists()) {
                Files.delete(invokeContext.getFinalPath());
            } else {
                Files.deleteIfExists(invokeContext.getFinalPath());
            }

            return null;
        } catch (IOException e) {
            throw newJFMException(e);
        }
    }

    @Override
    public Object handleListing(Listing listing) {
        try {
            Type type = invokeContext.getReturnType();
            ListingTypeHandler<?> typeHandler = typeHandlerService.getListingHandlerFor(type);
            return typeHandler.list(type, invokeContext.getFinalPath());
        } catch (IOException e) {
            throw newJFMException(e);
        }
    }
}
