package com.github.kjarosh.jfm.tests.pathparams;

import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Path;
import com.github.kjarosh.jfm.api.annotations.PathParam;
import com.github.kjarosh.jfm.api.annotations.Read;

@FilesystemResource
public interface PathParamsResource {
    @Read
    @Path("{name}")
    int getInteger(@PathParam("name") String name);
}
