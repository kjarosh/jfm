package com.github.kjarosh.jfm.tests.pathparams;

import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Listing;
import com.github.kjarosh.jfm.api.annotations.Path;
import com.github.kjarosh.jfm.api.annotations.PathParam;
import com.github.kjarosh.jfm.api.annotations.Read;

import java.util.List;
import java.util.stream.Stream;

@FilesystemResource
public interface PathParamsResource {
    @Read
    @Path("{name}")
    int getInteger(@PathParam("name") String name);

    @Read
    @Path("{name}/int")
    int getInnerInteger(@PathParam("name") String name);

    @Listing
    @Path("")
    List<String> listNames();

    @Listing
    @Path("")
    Stream<String> streamNames();
}
