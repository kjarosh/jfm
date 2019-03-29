package com.github.kjarosh.jfm.impl.proxy;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.Delete;
import com.github.kjarosh.jfm.api.annotations.Listing;
import com.github.kjarosh.jfm.api.annotations.Read;
import com.github.kjarosh.jfm.api.annotations.Write;
import com.github.kjarosh.jfm.api.annotations.WriteBoolean;
import com.github.kjarosh.jfm.api.annotations.WriteBytes;
import com.github.kjarosh.jfm.api.annotations.WriteInteger;
import com.github.kjarosh.jfm.api.annotations.WriteString;
import com.github.kjarosh.jfm.spi.types.ListingTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandlerService;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;

class FilesystemMapperMethodInvoker {
    private final TypeHandlerService typeHandlerService = FilesystemMapper.instance().getTypeHandlerService();
    private final InvokeContext invokeContext;

    FilesystemMapperMethodInvoker(InvokeContext invokeContext) {
        this.invokeContext = invokeContext;
    }

    private FilesystemMapperException newJFMException(IOException e) {
        return new FilesystemMapperException(
                "IO Exception while invoking a method " + invokeContext.getFullName(), e);
    }

    Object invokeRead(Read readAnnotation) {
        try {
            Type type = invokeContext.getReturnType();
            TypeHandler<?> returnTypeHandler = typeHandlerService.getHandlerFor(type);
            return returnTypeHandler.read(type, invokeContext.getFinalPath());
        } catch (IOException e) {
            throw newJFMException(e);
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
            returnTypeHandler.write(type, invokeContext.getFinalPath(), (T) content);
        } catch (IOException e) {
            throw newJFMException(e);
        }
    }

    private <T> void writeConstantValue(Class<T> type, T value) throws IOException {
        TypeHandler<T> returnTypeHandler = typeHandlerService.getHandlerFor(type);
        returnTypeHandler.write(type, invokeContext.getFinalPath(), value);
    }

    void invokeWriteBytes(WriteBytes writeBytes) {
        try {
            writeConstantValue(byte[].class, writeBytes.value());
        } catch (IOException e) {
            throw newJFMException(e);
        }
    }

    void invokeWriteString(WriteString writeString) {
        try {
            writeConstantValue(String.class, writeString.value());
        } catch (IOException e) {
            throw newJFMException(e);
        }
    }

    void invokeWriteBoolean(WriteBoolean writeBoolean) {
        try {
            writeConstantValue(boolean.class, writeBoolean.value());
        } catch (IOException e) {
            throw newJFMException(e);
        }
    }

    void invokeWriteInteger(WriteInteger writeInteger) {
        try {
            writeConstantValue(int.class, writeInteger.value());
        } catch (IOException e) {
            throw newJFMException(e);
        }
    }

    void invokeDelete(Delete delete) {
        try {
            if (delete.failOnExists()) {
                Files.delete(invokeContext.getFinalPath());
            } else {
                Files.deleteIfExists(invokeContext.getFinalPath());
            }
        } catch (IOException e) {
            throw newJFMException(e);
        }
    }

    Object invokeListing(Listing listing) {
        try {
            Type type = invokeContext.getReturnType();
            ListingTypeHandler<?> typeHandler = typeHandlerService.getListingHandlerFor(type);
            return typeHandler.list(type, invokeContext.getFinalPath());
        } catch (IOException e) {
            throw newJFMException(e);
        }
    }
}
