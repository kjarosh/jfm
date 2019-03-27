package com.github.kjarosh.jfm.impl;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.Content;
import com.github.kjarosh.jfm.api.annotations.PathParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class InvokeContext {
    private final Path basePath;
    private final Path finalPath;

    private final Method method;

    private Map<String, String> pathParams = new HashMap<>();
    private Object content = null;

    public InvokeContext(Path basePath, Method method, Object[] args) {
        this.basePath = basePath;
        this.method = method;
        segregateArguments(method, args);

        this.finalPath = new JfmPathResolver(basePath, pathParams).resolveMethod(method);
    }

    private void segregateArguments(Method method, Object[] args) {
        if (args == null) return;

        Parameter[] parameters = method.getParameters();
        int argc = args.length;
        for (int i = 0; i < argc; ++i) {
            segregateArgument(parameters[i], args[i]);
        }
    }

    private void segregateArgument(Parameter parameter, Object value) {
        if (parameter.isAnnotationPresent(PathParam.class)) {
            String paramName = parameter.getAnnotation(PathParam.class).value();
            if (pathParams.containsKey(paramName)) {
                throw new FilesystemMapperException(
                        "Duplicate path param: " + paramName + " on " + getFullName());
            }
            pathParams.put(paramName, String.valueOf(value));
        }

        if (parameter.isAnnotationPresent(Content.class)) {
            if (content != null) {
                throw new FilesystemMapperException(
                        "Duplicate content param on " + getFullName());

            }
            content = value;
        }
    }

    public Path getFinalPath() {
        return finalPath;
    }

    public Type getReturnType() {
        return method.getGenericReturnType();
    }

    public String getFullName() {
        return method.getDeclaringClass().getName() + "." + method.getName();
    }
}
