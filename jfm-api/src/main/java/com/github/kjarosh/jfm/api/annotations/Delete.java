package com.github.kjarosh.jfm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the invocation of the annotated method is akin to deleting
 * the file associated with it.
 * <p>
 * In case of a proxy, if the annotated method is invoked, the file is deleted.
 * In case of a mounted filesystem, if the file is deleted, the method is invoked.
 *
 * @author Kamil Jarosz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Delete {
    /**
     * @return {@code true} when the annotated method should fail when the file
     * does not exist, {@code false} otherwise
     */
    boolean failIfNotExists() default false;
}
