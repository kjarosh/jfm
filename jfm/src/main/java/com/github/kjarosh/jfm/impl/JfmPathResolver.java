package com.github.kjarosh.jfm.impl;

import com.github.kjarosh.jfm.api.annotations.Path;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

class JfmPathResolver {
    private java.nio.file.Path root;

    JfmPathResolver(java.nio.file.Path root) {
        this.root = root;
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

        String classPath = element.getAnnotation(Path.class).value();
        return base.resolve(classPath);
    }
}
