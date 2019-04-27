package com.github.kjarosh.jfm.impl.proxy;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.impl.AnnotationHandlingService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kamil Jarosz
 */
public class ResourceMethodInvocationHandler<T> implements InvocationHandler {
    private final AnnotationHandlingService annotationHandlingService = new AnnotationHandlingService();
    private final Path path;

    private final List<Method> notProxyable;
    private final Object notProxyableHandler;

    public ResourceMethodInvocationHandler(Class<T> resourceClass, Path path) {
        this.path = path;

        FilesystemResource resourceAnnotation = resourceClass
                .getAnnotation(FilesystemResource.class);

        if (resourceAnnotation == null) {
            throw new FilesystemMapperException(
                    "Resource class " + resourceClass + " is not annotated with "
                            + "@" + FilesystemResource.class.getSimpleName());
        }

        try {
            this.notProxyable = Arrays.asList(
                    Object.class.getMethod("toString"),
                    Object.class.getMethod("equals", Object.class),
                    Object.class.getMethod("hashCode"));
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }

        this.notProxyableHandler = new Object() {
            @Override
            public String toString() {
                return "JFMProxy[" + resourceClass + "]@" + Integer.toHexString(hashCode());
            }
        };
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (notProxyable.contains(method)) {
            try {
                return method.invoke(notProxyableHandler, args);
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }

        InvokeContext ic = new InvokeContext(path, method, args);
        ResourceMethodAnnotationHandler handler = new ResourceMethodAnnotationHandler(ic);
        return annotationHandlingService.handle(method, handler);
    }
}
