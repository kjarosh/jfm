package com.github.kjarosh.jfm.impl.mounter.rproxy;

import com.github.kjarosh.jfm.impl.MethodHandlingService;
import com.github.kjarosh.jfm.impl.UnsupportedMethodException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
        for (Method method : methods) {
            try {
                byte[] read = methodHandlingService.handle(method,
                        new ReverseProxyReadHandler(method, resource));

                if (read != null) return read;
            } catch (UnsupportedMethodException e) {
                continue;
            }
        }

        throw new NoHandlerMethodException("No method to handle read: " + path);
    }

    public void write(byte[] data) {
        for (Method method : methods) {
            try {
                methodHandlingService.handle(method,
                        new ReverseProxyWriteHandler(method, resource, data));
            } catch (UnsupportedMethodException e) {
                continue;
            }
        }

        throw new NoHandlerMethodException("No method to handle write: " + path);
    }

    public void addHandlingMethod(Method method) {
        methods.add(method);
    }
}
