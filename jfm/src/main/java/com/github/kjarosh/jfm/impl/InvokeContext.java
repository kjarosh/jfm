package com.github.kjarosh.jfm.impl;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.Content;
import com.github.kjarosh.jfm.api.annotations.PathParam;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class InvokeContext {
    private final Path finalPath;
    private final Method method;

    private Map<String, String> pathParams = new HashMap<>();
    private Object content = null;
    private Type contentType = null;

    InvokeContext(Path basePath, Method method, Object[] args) {
        this.method = method;
        parseArguments(method, args);

        this.finalPath = new JfmPathResolver(basePath, pathParams).resolveMethod(method);
    }

    private void parseArguments(Method method, Object[] args) {
        if (args == null) return;

        Parameter[] parameters = method.getParameters();
        int argc = args.length;
        for (int i = 0; i < argc; ++i) {
            parseArgument(parameters[i], args[i]);
        }
    }

    private void parseArgument(Parameter parameter, Object value) {
        if (parameter.isAnnotationPresent(PathParam.class)) {
            PathParam pathParam = parameter.getAnnotation(PathParam.class);
            String paramName = pathParam.value();
            if (pathParams.containsKey(paramName)) {
                throw new FilesystemMapperException(
                        "Duplicate path param: " + paramName + " on " + getFullName());
            }

            String paramValue = String.valueOf(value);
            if (!pathParam.allowSeparators() &&
                    (paramValue.contains("/") || paramValue.contains(File.separator))) {
                throw new FilesystemMapperException(
                        "Parameter value contains separators: " + paramValue);
            }
            pathParams.put(paramName, paramValue);
        }

        if (parameter.isAnnotationPresent(Content.class)) {
            if (content != null) {
                throw new FilesystemMapperException(
                        "Duplicate content param on " + getFullName());

            }
            content = value;
            contentType = parameter.getParameterizedType();
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

    public Type getContentType() {
        return contentType;
    }

    public Object getContent() {
        return content;
    }
}
