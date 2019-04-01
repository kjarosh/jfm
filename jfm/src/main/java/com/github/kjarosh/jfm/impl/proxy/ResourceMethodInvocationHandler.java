package com.github.kjarosh.jfm.impl.proxy;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.impl.MethodHandlingService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.file.Path;

/**
 * @author Kamil Jarosz
 */
public class ResourceMethodInvocationHandler<T> implements InvocationHandler {
    private final MethodHandlingService methodHandlingService = new MethodHandlingService();
    private final Path path;

    public ResourceMethodInvocationHandler(Class<T> resourceClass, Path path) {
        this.path = path;

        FilesystemResource resourceAnnotation = resourceClass
                .getAnnotation(FilesystemResource.class);

        if (resourceAnnotation == null) {
            throw new FilesystemMapperException("Resource class is not annotated with " +
                    "@" + FilesystemResource.class.getSimpleName());
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        InvokeContext ic = new InvokeContext(path, method, args);
        ResourceMethodProxy invoker = new ResourceMethodProxy(ic);
        return methodHandlingService.handle(method, invoker);
    }
}
