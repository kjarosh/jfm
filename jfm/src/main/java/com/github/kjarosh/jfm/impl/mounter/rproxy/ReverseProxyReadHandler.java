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

/**
 * @author Kamil Jarosz
 */
public class ReverseProxyReadHandler implements MethodHandler<byte[]> {
    private final TypeHandlerService typeHandlerService = FilesystemMapper.instance()
            .getTypeHandlerService();
    private final ResourceMethodInvoker invoker;

    public ReverseProxyReadHandler(Method method, Object resource) {
        this.invoker = new ResourceMethodInvoker(method, resource);
    }

    @Override
    public byte[] handleRead(Read read) {
        return handleRead0(read);
    }

    @SuppressWarnings("unchecked")
    private <T> byte[] handleRead0(Read read) {
        Object readObject = invoker.invoke();
        Type type = invoker.getReturnType();
        TypeHandler<T> returnTypeHandler = (TypeHandler<T>) typeHandlerService.getHandlerFor(type);
        return returnTypeHandler.serialize(type, (T) readObject);
    }

    @Override
    public byte[] handleWrite(Write write) {
        return null;
    }

    @Override
    public byte[] handleWriteBytes(WriteBytes writeBytes) {
        return null;
    }

    @Override
    public byte[] handleWriteString(WriteString writeString) {
        return null;
    }

    @Override
    public byte[] handleWriteBoolean(WriteBoolean writeBoolean) {
        return null;
    }

    @Override
    public byte[] handleWriteInteger(WriteInteger writeInteger) {
        return null;
    }

    @Override
    public byte[] handleDelete(Delete delete) {
        return null;
    }

    @Override
    public byte[] handleListing(Listing listing) {
        return null;
    }
}
