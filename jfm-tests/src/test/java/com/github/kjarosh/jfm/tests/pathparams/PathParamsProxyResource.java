package com.github.kjarosh.jfm.tests.pathparams;

import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Path;
import com.github.kjarosh.jfm.api.annotations.PathParam;
import com.github.kjarosh.jfm.api.annotations.Read;

@FilesystemResource
public interface PathParamsProxyResource {
    @Read
    @Path("{name}")
    int getInteger(@PathParam("name") String name);

    @Read
    @Path("{name}/int")
    int getInnerInteger(@PathParam("name") String name);

    @Read
    @Path("{path}")
    int getAnyInteger(@PathParam(value = "path", allowSeparators = true) String path);

    @Read
    @Path("{name}")
    int getIntegerWithRegex(@PathParam(value = "name", regex = "[a-z/]+") String name);
}
