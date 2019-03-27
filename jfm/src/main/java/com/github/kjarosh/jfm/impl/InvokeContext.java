package com.github.kjarosh.jfm.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.file.Path;

public class InvokeContext {
    private final Path basePath;
    private final Path finalPath;

    private final Method method;
    private final Object[] args;

    public InvokeContext(Path basePath, Method method, Object[] args) {
        this.basePath = basePath;
        this.method = method;
        this.args = args;

        this.finalPath = new JfmPathResolver(basePath).resolveMethod(method);
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
