package com.github.kjarosh.jfm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Kamil Jarosz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PathParam {
    /**
     * @return name of the parameter used in the path
     */
    String value();

    /**
     * If set to {@code true}, the file separators are not allowed in this
     * parameter, i.e.
     * <ul>
     * <li>(in case of a proxy)
     * when invoking a method with a separator an exception is thrown,</li>
     * <li>(in case of a mounted filesystem)
     * method will not be resolved in a way such that the parameter would
     * contain a separator.</li>
     * </ul>
     * <p>
     * Setting this to {@code false} is equivalent to forbidding file separators
     * in {@link #regex()}, but from a security standpoint this method is
     * separated.
     *
     * @return {@code true} is separators should be allowed, {@code false} otherwise
     */
    boolean allowSeparators() default false;

    /**
     * Restrict values of this path parameter to only these conforming to the regex.
     * <p>
     * Take into consideration the fact that an empty string and a null value
     * are already forbidden.
     *
     * @return the regex which the value should conform to
     */
    String regex() default ".*";
}
