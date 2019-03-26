package com.github.kjarosh.jfm.examples;

import com.github.kjarosh.jfm.api.annotations.Content;
import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Path;
import com.github.kjarosh.jfm.api.annotations.Read;
import com.github.kjarosh.jfm.api.annotations.Write;

@FilesystemResource
public interface PersonInfoResource {
    @Read
    @Path("first-name")
    String getFirstName();

    @Write
    @Path("first-name")
    void setFirstName(@Content String firstName);

    @Path("last-name")
    String getLastName();

    default String getName() {
        return getFirstName() + " " + getLastName();
    }
}
