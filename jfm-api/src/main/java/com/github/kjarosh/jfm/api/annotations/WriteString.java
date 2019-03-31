package com.github.kjarosh.jfm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is equivalent to {@link Write} with the content
 * equal to the {@link #value()}.
 *
 * @author Kamil Jarosz
 * @see WriteBoolean
 * @see WriteBytes
 * @see WriteInteger
 * @see Write
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WriteString {
    String value();
}
