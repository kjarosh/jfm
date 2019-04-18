package com.github.kjarosh.jfm.impl.mounter.rproxy;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.annotations.Read;
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

    ReverseProxyReadHandler(Method method, Object resource) {
        this.invoker = new ResourceMethodInvoker(method, resource);
    }

    @Override
    public byte[] handleRead(Read read) {
        return handleRead0(read);
    }

    @SuppressWarnings("unchecked")
    private <T> byte[] handleRead0(Read read) {
        Object readObject = invoker.invoke();
        if (readObject == null) {
            return new byte[0];
        }

        Type type = invoker.getReturnType();
        TypeHandler<T> returnTypeHandler = (TypeHandler<T>) typeHandlerService.getHandlerFor(type);
        return returnTypeHandler.serialize(type, (T) readObject);
    }
}
