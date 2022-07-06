package com.github.kjarosh.jfm.impl.annotation;

import com.github.kjarosh.jfm.api.annotations.Delete;
import com.github.kjarosh.jfm.api.annotations.Listing;
import com.github.kjarosh.jfm.api.annotations.Read;
import com.github.kjarosh.jfm.api.annotations.Write;
import com.github.kjarosh.jfm.api.annotations.WriteBoolean;
import com.github.kjarosh.jfm.api.annotations.WriteBytes;
import com.github.kjarosh.jfm.api.annotations.WriteInteger;
import com.github.kjarosh.jfm.api.annotations.WriteString;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Kamil Jarosz
 */
public class AnnotatedMethod {
    private static final List<Class<? extends Annotation>> INTENT_ANNOTATION_CLASSES = Arrays.asList(
            Read.class,
            Write.class,
            WriteString.class,
            WriteBytes.class,
            WriteBoolean.class,
            WriteInteger.class,
            Delete.class,
            Listing.class);
    private final List<Annotation> intentAnnotations;

    public static AnnotatedMethod of(Method method) {
        return new AnnotatedMethod(method);
    }

    private AnnotatedMethod(Method method) {
        intentAnnotations = new ArrayList<>();
        for (Class<? extends Annotation> clazz : INTENT_ANNOTATION_CLASSES) {
            Annotation annotation = method.getAnnotation(clazz);
            if (annotation != null) {
                intentAnnotations.add(annotation);
            }
        }
    }

    public List<Annotation> getIntentAnnotations() {
        return Collections.unmodifiableList(intentAnnotations);
    }

    public boolean isAnnotated() {
        return !intentAnnotations.isEmpty();
    }
}
