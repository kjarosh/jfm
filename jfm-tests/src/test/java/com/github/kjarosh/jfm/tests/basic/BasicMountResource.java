package com.github.kjarosh.jfm.tests.basic;

import com.github.kjarosh.jfm.api.annotations.Content;
import com.github.kjarosh.jfm.api.annotations.Delete;
import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Path;
import com.github.kjarosh.jfm.api.annotations.Read;
import com.github.kjarosh.jfm.api.annotations.Write;

import java.util.Optional;
import java.util.OptionalInt;

@FilesystemResource
public interface BasicMountResource {
    @Read
    @Path("name")
    String getName();

    @Read
    @Path("optional-name")
    Optional<String> getOptionalName();

    @Read
    @Path("number")
    int getInt();

    @Read
    @Path("optional-number")
    OptionalInt getOptionalInt();

    @Write
    @Path("optional-number")
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    void setOptionalInt(@Content OptionalInt value);

    @Delete
    @Path("removable-string")
    void removeString();
}
