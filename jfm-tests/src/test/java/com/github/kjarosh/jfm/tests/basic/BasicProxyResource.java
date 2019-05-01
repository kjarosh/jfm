package com.github.kjarosh.jfm.tests.basic;

import com.github.kjarosh.jfm.api.annotations.Content;
import com.github.kjarosh.jfm.api.annotations.Delete;
import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Path;
import com.github.kjarosh.jfm.api.annotations.Read;
import com.github.kjarosh.jfm.api.annotations.Write;
import com.github.kjarosh.jfm.api.annotations.WriteBoolean;
import com.github.kjarosh.jfm.api.annotations.WriteBytes;
import com.github.kjarosh.jfm.api.annotations.WriteInteger;
import com.github.kjarosh.jfm.api.annotations.WriteString;

import java.util.Optional;
import java.util.OptionalInt;

@FilesystemResource
public interface BasicProxyResource {
    @Read
    @Path("string")
    String getString();

    @Read
    @Path("optional-string")
    Optional<String> getOptionalString();

    @Read
    @Path("optional-string-empty")
    Optional<String> getOptionalStringEmpty();

    @Read
    @Path("optional-int")
    OptionalInt getOptionalInt();

    @Write
    @Path("optional-int")
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    void setOptionalInt(@Content OptionalInt value);

    @Read
    @Path("optional-int-empty")
    OptionalInt getOptionalIntEmpty();

    @Read
    @Path("int")
    int getInt();

    @Read
    @Path("invalid-int")
    int getInvalidInt();

    @Read
    @Path("integer")
    Integer getInteger();

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

    @Delete
    @Path("removable-file")
    void removeFile();

    @WriteBytes({'a', 's', 'd', 'f'})
    @Path("constant-bytes")
    void setConstantBytes();

    @WriteInteger(7)
    @Path("constant-int")
    void setConstantInt();

    @WriteBoolean(true)
    @Path("constant-boolean")
    void setConstantBoolean();

    @WriteString("asdf")
    @Path("constant-string")
    void setConstantString();
}
