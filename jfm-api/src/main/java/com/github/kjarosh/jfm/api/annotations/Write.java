package com.github.kjarosh.jfm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the invocation of the annotated method is akin to writing
 * data to the file associated with it.
 * <p>
 * If a method is annotated with {@link Write}, it also needs to have a parameter
 * annotated with {@link Content} in order to supply the data to write.
 * <p>
 * In case of a proxy, if the annotated method is invoked, the supplied content
 * is written into the file.
 * In case of a mounted filesystem, if data is written into the file, the method
 * is invoked with the written data.
 *
 * @author Kamil Jarosz
 * @see WriteBoolean
 * @see WriteBytes
 * @see WriteInteger
 * @see WriteString
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Write {

}
