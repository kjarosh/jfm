package com.github.kjarosh.jfm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the path associated with the annotated method.
 * <p>
 * Values in a form of
 * <code>&#123;</code><i>name</i><code>&#125;</code>
 * are bound to the corresponding
 * {@link PathParam}s if provided.
 *
 * @author Kamil Jarosz
 * @see Read
 * @see Write
 * @see Listing
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Path {
    String value();
}
