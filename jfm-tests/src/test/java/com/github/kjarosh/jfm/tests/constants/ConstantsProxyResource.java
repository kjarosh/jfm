package com.github.kjarosh.jfm.tests.constants;

import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Path;
import com.github.kjarosh.jfm.api.annotations.WriteBoolean;
import com.github.kjarosh.jfm.api.annotations.WriteBytes;
import com.github.kjarosh.jfm.api.annotations.WriteInteger;
import com.github.kjarosh.jfm.api.annotations.WriteString;

@FilesystemResource
public interface ConstantsProxyResource {
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
