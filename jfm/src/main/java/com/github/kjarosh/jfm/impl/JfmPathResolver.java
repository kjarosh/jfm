package com.github.kjarosh.jfm.impl;

import com.github.kjarosh.jfm.api.annotations.Path;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Map;

class JfmPathResolver {
    private java.nio.file.Path root;
    private Map<String, String> pathParams;

    JfmPathResolver(java.nio.file.Path root, Map<String, String> pathParams) {
        this.root = root;
        this.pathParams = pathParams;
    }

    java.nio.file.Path resolveMethod(Method method) {
        return resolve(resolveClass(method.getDeclaringClass()), method);
    }

    java.nio.file.Path resolveClass(Class<?> clazz) {
        return resolve(root, clazz);
    }

    private java.nio.file.Path resolve(java.nio.file.Path base, AnnotatedElement element) {
        if (!element.isAnnotationPresent(Path.class)) {
            return base;
        }

        String relativePath = element.getAnnotation(Path.class).value();
        return base.resolve(replacePathParams(relativePath));
    }

    private String replacePathParams(String relativePath) {
        for (Map.Entry<String, String> entry : pathParams.entrySet()) {
            relativePath = relativePath.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return relativePath;
    }
}
