package com.github.kjarosh.jfm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the invocation of the annotated method is akin to listing
 * the directory associated with it.
 * <p>
 * If a method is annotated with {@link Listing}, it should also have a return
 * type declared.
 * <p>
 * In case of a proxy, if the annotated method is invoked, the directory is
 * listed and the listing is returned by the method.
 * In case of a mounted filesystem, if the directory is listed, the listing
 * takes data from the annotated method.
 *
 * @author Kamil Jarosz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Listing {
    /**
     * If set to {@code true}:
     * <ul>
     * <li>(in case of a proxy)
     * the returned listing doesn't include declared entries,</li>
     * <li>(in case of a mounted filesystem)
     * declared entries are appended to the listing returned by the method.</li>
     * </ul>
     *
     * @return {@code true} if declared entries should be handled,
     * {@code false} otherwise
     */
    boolean handleDeclaredEntries() default true;
}
