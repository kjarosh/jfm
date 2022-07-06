package com.github.kjarosh.jfm.impl.annotation;

import com.github.kjarosh.jfm.api.FilesystemMapperException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Kamil Jarosz
 */
public class AnnotationHandlingService {
    public <R> R handle(Method method, AnnotationHandler<R> handler) {
        AnnotatedMethod annotatedMethod = AnnotatedMethod.of(method);

        List<Annotation> intentAnnotations = annotatedMethod.getIntentAnnotations();
        if (intentAnnotations.size() == 0) {
            throw new FilesystemMapperException(
                    "Method " + getMethodFullName(method) + " is not annotated with any JFM annotation");
        }

        if (intentAnnotations.size() > 1) {
            throw new FilesystemMapperException(
                    "Method " + getMethodFullName(method) + " is multiply annotated");
        }

        Annotation intentAnnotation = intentAnnotations.get(0);
        return handler.handle(intentAnnotation);
    }

    private String getMethodFullName(Method method) {
        return method.getDeclaringClass().getName() + "." + method.getName();
    }
}
