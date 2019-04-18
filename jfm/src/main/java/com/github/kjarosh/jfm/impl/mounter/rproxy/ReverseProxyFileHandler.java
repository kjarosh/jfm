package com.github.kjarosh.jfm.impl.mounter.rproxy;

import com.github.kjarosh.jfm.impl.MethodHandler;
import com.github.kjarosh.jfm.impl.MethodHandlingService;
import com.github.kjarosh.jfm.impl.UnsupportedMethodException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Kamil Jarosz
 */
public class ReverseProxyFileHandler {
    private final MethodHandlingService methodHandlingService = new MethodHandlingService();
    private final List<Method> methods = new ArrayList<>();
    private final String path;
    private final Object resource;

    ReverseProxyFileHandler(String path, Object resource) {
        this.path = path;
        this.resource = resource;
    }

    public byte[] read() {
        return handle("read", method -> new ReverseProxyReadHandler(method, resource));
    }

    public void write(byte[] data) {
        handle("write", method -> new ReverseProxyWriteHandler(method, resource, data));
    }

    public void delete() {
        handle("delete", method -> new ReverseProxyDeleteHandler(method, resource));
    }

    public <T> T handle(String what, Function<Method, MethodHandler<T>> handler) {
        for (Method method : methods) {
            try {
                return methodHandlingService.handle(method, handler.apply(method));
            } catch (UnsupportedMethodException e) {
                continue;
            }
        }

        throw new NoHandlerMethodException("No method to handle " + what + ": " + path);
    }

    public void addHandlingMethod(Method method) {
        methods.add(method);
    }
}
