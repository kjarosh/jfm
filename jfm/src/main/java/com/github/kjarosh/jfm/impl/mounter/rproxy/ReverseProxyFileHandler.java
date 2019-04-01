package com.github.kjarosh.jfm.impl.mounter.rproxy;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.impl.MethodHandlingService;

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
            byte[] read = methodHandlingService.handle(method,
                    new ReverseProxyReadHandler(method, resource));

            if (read != null) return read;
        }

        throw new FilesystemMapperException("No method to handle write to: " + path);
    }

    public void write(byte[] data) {
        for (Method method : methods) {
            boolean success = methodHandlingService.handle(method,
                    new ReverseProxyWriteHandler(method, resource, data));

            if (success) return;
        }

        throw new FilesystemMapperException("No method to handle write to: " + path);
    }

    public void addHandlingMethod(Method method) {
        methods.add(method);
    }
}
