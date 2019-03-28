package com.github.kjarosh.jfm.api;

public interface FilesystemMapperTarget {
    <T> T proxy(Class<T> resourceClass);

    void mount(Object resource);
}
