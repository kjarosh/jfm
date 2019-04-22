package com.github.kjarosh.jfm.tests.methodresolving;

import com.github.kjarosh.jfm.api.annotations.Content;
import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Path;
import com.github.kjarosh.jfm.api.annotations.Write;

@FilesystemResource
public interface MethodResolvingMountResource {
    @Write
    @Path("value")
    void setInteger(@Content int value);

    @Write
    @Path("value")
    void setString(@Content String value);
}
