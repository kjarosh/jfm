package com.github.kjarosh.jfm.impl.proxy;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.Content;
import com.github.kjarosh.jfm.api.annotations.PathParam;
import com.github.kjarosh.jfm.impl.util.PathParamValidator;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kamil Jarosz
 */
class InvokeContext {
    private final PathParamValidator pathParamValidator = new PathParamValidator();

    private final Path finalPath;
    private final Method method;

    private final Map<String, String> pathParams = new HashMap<>();
    private Object content = null;
    private Type contentType = null;

    InvokeContext(Path basePath, Method method, Object[] args) {
        this.method = method;
        parseArguments(method, args);

        this.finalPath = new ProxyPathResolver(basePath, pathParams).resolveMethod(method);
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
            if (paramName.isEmpty()) {
                throw new FilesystemMapperException(
                        "Empty path param name on " + getFullName());
            }

            if (pathParams.containsKey(paramName)) {
                throw new FilesystemMapperException(
                        "Duplicate path param: " + paramName + " on " + getFullName());
            }

            String paramValue = String.valueOf(value);
            pathParamValidator.validate(pathParam, paramValue);
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

    public boolean isContentAvailable() {
        return contentType != null;
    }

    public Type getContentType() {
        return contentType;
    }

    public Object getContent() {
        return content;
    }
}
