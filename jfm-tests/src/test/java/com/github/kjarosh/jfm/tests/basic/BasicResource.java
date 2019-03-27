package com.github.kjarosh.jfm.tests.basic;

import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Path;
import com.github.kjarosh.jfm.api.annotations.Read;

import java.util.Optional;
import java.util.OptionalInt;

@FilesystemResource
public interface BasicResource {
    @Read
    @Path("name")
    String getName();

    @Read
    @Path("name")
    Optional<String> getOptionalName();

    @Read
    @Path("non-existing-file")
    Optional<String> getOptionalEmpty();

    @Read
    @Path("number")
    int getInt();

    @Read
    @Path("number")
    OptionalInt getOptionalInt();

    @Read
    @Path("number-that-does-not-exist")
    OptionalInt getOptionalIntEmpty();

    @Read
    @Path("number")
    Integer getInteger();

    @Read
    @Path("invalid-number")
    int getInvalidInteger();

    @Read
    @Path("byte")
    byte getByte();

    @Read
    @Path("char")
    char getChar();

    @Read
    @Path("float")
    float getFloat();

    @Read
    @Path("double")
    double getDouble();
}
