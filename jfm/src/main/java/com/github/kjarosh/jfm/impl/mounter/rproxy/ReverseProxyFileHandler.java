package com.github.kjarosh.jfm.impl.mounter.rproxy;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.impl.MethodHandlingService;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
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

    public byte[] read() throws NoHandlerMethodException {
        List<Throwable> suppressed = new ArrayList<>();
        for (Method method : methods) {
            try {
                byte[] read = methodHandlingService.handle(method,
                        new ReverseProxyReadHandler(method, resource));

                if (read != null) return read;
            } catch (FilesystemMapperException e) {
                suppressed.add(e);
            }
        }

        Date deadline = Date.from(Instant.now().plusSeconds(120));
        while (new Date().before(deadline)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        NoHandlerMethodException toThrow =
                new NoHandlerMethodException("No method to handle read: " + path);
        suppressed.forEach(toThrow::addSuppressed);
        throw toThrow;
    }

    public void write(byte[] data) throws NoHandlerMethodException {
        List<Throwable> suppressed = new ArrayList<>();
        for (Method method : methods) {
            try {
                boolean success = methodHandlingService.handle(method,
                        new ReverseProxyWriteHandler(method, resource, data));

                if (success) return;
            } catch (FilesystemMapperException e) {
                suppressed.add(e);
            }
        }

        NoHandlerMethodException toThrow =
                new NoHandlerMethodException("No method to handle write: " + path);
        suppressed.forEach(toThrow::addSuppressed);
        throw toThrow;
    }

    public void addHandlingMethod(Method method) {
        methods.add(method);
    }
}
