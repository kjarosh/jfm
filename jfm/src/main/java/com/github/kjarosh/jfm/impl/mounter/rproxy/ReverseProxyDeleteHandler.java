package com.github.kjarosh.jfm.impl.mounter.rproxy;

import com.github.kjarosh.jfm.api.annotations.Delete;
import com.github.kjarosh.jfm.impl.MethodHandler;

import java.lang.reflect.Method;

/**
 * @author Kamil Jarosz
 */
public class ReverseProxyDeleteHandler implements MethodHandler<Void> {
    private final ResourceMethodInvoker invoker;

    ReverseProxyDeleteHandler(Method method, Object resource) {
        this.invoker = new ResourceMethodInvoker(method, resource);
    }

    @Override
    public Void handleDelete(Delete delete) {
        invoker.invoke();
        return null;
    }
}
