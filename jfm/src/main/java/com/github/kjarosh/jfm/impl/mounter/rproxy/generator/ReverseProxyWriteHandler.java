package com.github.kjarosh.jfm.impl.mounter.rproxy.generator;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.annotations.Write;
import com.github.kjarosh.jfm.api.annotations.WriteBoolean;
import com.github.kjarosh.jfm.api.annotations.WriteBytes;
import com.github.kjarosh.jfm.api.annotations.WriteInteger;
import com.github.kjarosh.jfm.api.annotations.WriteString;
import com.github.kjarosh.jfm.impl.AnnotationHandler;
import com.github.kjarosh.jfm.impl.UnsupportedAnnotationException;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandlerService;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author Kamil Jarosz
 */
public class ReverseProxyWriteHandler implements AnnotationHandler<Void> {
    private final TypeHandlerService typeHandlerService = FilesystemMapper.instance()
            .getTypeHandlerService();
    private final ResourceMethodInvoker invoker;
    private final byte[] data;

    ReverseProxyWriteHandler(Method method, Object resource, byte[] data) {
        this.invoker = new ResourceMethodInvoker(method, resource);
        this.data = data;
    }

    @Override
    public Void handleWrite(Write write) {
        Type type = invoker.getContentType();
        TypeHandler<?> returnTypeHandler = typeHandlerService.getHandlerFor(type);
        Object content = returnTypeHandler.deserialize(type, data);
        invoker.setContent(content);
        invoker.invoke();
        return null;
    }

    private void handleWriteBytes(byte[] bytes) {
        if (!Arrays.equals(bytes, data)) {
            throw new UnsupportedAnnotationException();
        }

        invoker.invoke();
    }

    @Override
    public Void handleWriteBytes(WriteBytes writeBytes) {
        handleWriteBytes(writeBytes.value());
        return null;
    }

    @Override
    public Void handleWriteString(WriteString writeString) {
        byte[] bytes = writeString.value().getBytes(StandardCharsets.UTF_8);
        handleWriteBytes(bytes);
        return null;
    }

    @Override
    public Void handleWriteBoolean(WriteBoolean writeBoolean) {
        byte[] bytes = Boolean.toString(writeBoolean.value()).getBytes();
        handleWriteBytes(bytes);
        return null;
    }

    @Override
    public Void handleWriteInteger(WriteInteger writeInteger) {
        byte[] bytes = Integer.toString(writeInteger.value()).getBytes();
        handleWriteBytes(bytes);
        return null;
    }
}
