package com.github.kjarosh.jfm.tests.basic;

import com.github.kjarosh.jfm.api.annotations.Content;
import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Path;
import com.github.kjarosh.jfm.api.annotations.Read;
import com.github.kjarosh.jfm.api.annotations.Write;
import com.github.kjarosh.jfm.api.annotations.WriteBytes;

import java.util.Optional;
import java.util.OptionalInt;

@FilesystemResource
public interface BasicProxyResource {
    @Read
    @Path("name")
    String getName();

    @Read
    @Path("optional-name")
    Optional<String> getOptionalName();

    @Read
    @Path("optional-empty")
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

    @WriteBytes(value = {})
    @Path("optional-number")
    void emptyOptionalInt();

    @Read
    @Path("optional-number-empty")
    OptionalInt getOptionalIntEmpty();

    @Read
    @Path("integer")
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
