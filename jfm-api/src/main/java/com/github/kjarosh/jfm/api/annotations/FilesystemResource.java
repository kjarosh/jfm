package com.github.kjarosh.jfm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a filesystem resource. Interfaces annotated with it supply methods
 * used to map files into objects and vice versa.
 * <p>
 * See the documentations of the corresponding method annotations.
 *
 * @author Kamil Jarosz
 * @see Read
 * @see Write
 * @see Delete
 * @see Listing
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FilesystemResource {

}
