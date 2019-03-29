package com.github.kjarosh.jfm.api;

/**
 * @author Kamil Jarosz
 */
public interface FilesystemMapperTarget {
    <T> T proxy(Class<T> resourceClass);

    void mount(Object resource);
}
