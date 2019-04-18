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
public interface BasicResource {
    @Read
    @Path("name")
    String getName();

    @Read
    @Path("optional-name")
    Optional<String> getOptionalName();

    @Read
    @Path("non-existing-file")
    Optional<String> getOptionalEmpty();

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
    @Path("optional-number")
    void removeOptionalInt();

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

    @Write
    @Path("byte")
    void setByte(@Content byte value);

    @Read
    @Path("char")
    char getChar();

    @Write
    @Path("char")
    void setChar(@Content char value);

    @Read
    @Path("float")
    float getFloat();

    @Write
    @Path("float")
    void setFloat(@Content float value);

    @Read
    @Path("double")
    double getDouble();

    @Write
    @Path("double")
    void setDouble(@Content double value);
}
