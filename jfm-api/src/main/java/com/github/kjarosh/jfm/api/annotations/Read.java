package com.github.kjarosh.jfm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the invocation of the annotated method is akin to reading
 * data from the file associated with it.
 * <p>
 * If a method is annotated with {@link Read}, it should also have a return
 * type declared.
 * <p>
 * In case of a proxy, if the annotated method is invoked, the file is read
 * and its contents are returned by the method.
 * In case of a mounted filesystem, if data is read from the file, the method is
 * invoked and the returned value is passed as the result.
 *
 * @author Kamil Jarosz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Read {

}
