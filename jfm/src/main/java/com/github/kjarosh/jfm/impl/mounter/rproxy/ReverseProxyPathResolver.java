package com.github.kjarosh.jfm.impl.mounter.rproxy;

import com.github.kjarosh.jfm.api.annotations.Path;
import com.github.kjarosh.jfm.impl.util.PathUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * @author Kamil Jarosz
 */
class ReverseProxyPathResolver {
    private final Method method;

    ReverseProxyPathResolver(Method method) {
        this.method = method;
    }

    String resolve() {
        return resolve(resolveClass(method.getDeclaringClass()), method);
    }

    private String resolveClass(Class<?> clazz) {
        return resolve("/", clazz);
    }

    private String resolve(String base, AnnotatedElement element) {
        if (!element.isAnnotationPresent(Path.class)) {
            return base;
        }

        String relativePath = element.getAnnotation(Path.class).value();
        return PathUtils.toDirPath(base) + relativePath;
    }
}
