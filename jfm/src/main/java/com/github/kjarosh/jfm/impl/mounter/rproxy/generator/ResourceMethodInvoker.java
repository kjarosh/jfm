package com.github.kjarosh.jfm.impl.mounter.rproxy.generator;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.Content;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

/**
 * @author Kamil Jarosz
 */
public class ResourceMethodInvoker {
    private final Method method;
    private Object resource;

    private Type contentType;
    private int contentPosition;
    private Object content;

    ResourceMethodInvoker(Method method, Object resource) {
        this.method = method;
        this.resource = resource;

        int position = 0;
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(Content.class)) {
                contentType = parameter.getParameterizedType();
                contentPosition = position;
            }

            ++position;
        }
    }

    public Type getContentType() {
        return contentType;
    }

    public Type getReturnType() {
        return method.getGenericReturnType();
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Object invoke() {
        Object[] args = new Object[method.getParameterCount()];
        if (content != null) {
            args[contentPosition] = content;
        }

        try {
            return method.invoke(resource, args);
        } catch (IllegalAccessException e) {
            throw new FilesystemMapperException("Cannot access method", e);
        } catch (InvocationTargetException e) {
            throw new FilesystemMapperException("Method threw an exception", e);
        }
    }
}
