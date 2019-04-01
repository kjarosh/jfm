package com.github.kjarosh.jfm.impl.mounter.rproxy;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.annotations.Delete;
import com.github.kjarosh.jfm.api.annotations.Listing;
import com.github.kjarosh.jfm.api.annotations.Read;
import com.github.kjarosh.jfm.api.annotations.Write;
import com.github.kjarosh.jfm.api.annotations.WriteBoolean;
import com.github.kjarosh.jfm.api.annotations.WriteBytes;
import com.github.kjarosh.jfm.api.annotations.WriteInteger;
import com.github.kjarosh.jfm.api.annotations.WriteString;
import com.github.kjarosh.jfm.impl.MethodHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandlerService;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author Kamil Jarosz
 */
public class ReverseProxyWriteHandler implements MethodHandler<Boolean> {
    private final TypeHandlerService typeHandlerService = FilesystemMapper.instance()
            .getTypeHandlerService();
    private final ResourceMethodInvoker invoker;
    private final byte[] data;

    ReverseProxyWriteHandler(Method method, Object resource, byte[] data) {
        this.invoker = new ResourceMethodInvoker(method, resource);
        this.data = data;
    }

    @Override
    public Boolean handleRead(Read read) {
        return false;
    }

    @Override
    public Boolean handleWrite(Write write) {
        Type type = invoker.getContentType();
        TypeHandler<?> returnTypeHandler = typeHandlerService.getHandlerFor(type);
        Object content = returnTypeHandler.deserialize(type, data);
        invoker.setContent(content);
        invoker.invoke();
        return true;
    }

    private Boolean handleWriteBytes(byte[] bytes) {
        if (Arrays.equals(bytes, data)) {
            invoker.invoke();
            return true;
        }

        return false;
    }

    @Override
    public Boolean handleWriteBytes(WriteBytes writeBytes) {
        return handleWriteBytes(writeBytes.value());
    }

    @Override
    public Boolean handleWriteString(WriteString writeString) {
        byte[] bytes = writeString.value().getBytes(StandardCharsets.UTF_8);
        return handleWriteBytes(bytes);
    }

    @Override
    public Boolean handleWriteBoolean(WriteBoolean writeBoolean) {
        byte[] bytes = Boolean.toString(writeBoolean.value()).getBytes();
        return handleWriteBytes(bytes);
    }

    @Override
    public Boolean handleWriteInteger(WriteInteger writeInteger) {
        byte[] bytes = Integer.toString(writeInteger.value()).getBytes();
        return handleWriteBytes(bytes);
    }

    @Override
    public Boolean handleDelete(Delete delete) {
        return false;
    }

    @Override
    public Boolean handleListing(Listing listing) {
        return false;
    }
}
