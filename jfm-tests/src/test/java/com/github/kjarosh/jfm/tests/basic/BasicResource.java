package com.github.kjarosh.jfm.tests.basic;

import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Path;
import com.github.kjarosh.jfm.api.annotations.Read;

@FilesystemResource
public interface BasicResource {
    @Read
    @Path("name")
    String getName();
}
