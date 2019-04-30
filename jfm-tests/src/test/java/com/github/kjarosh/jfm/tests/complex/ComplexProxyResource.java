package com.github.kjarosh.jfm.tests.complex;

import com.github.kjarosh.jfm.api.annotations.Content;
import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Path;
import com.github.kjarosh.jfm.api.annotations.Read;
import com.github.kjarosh.jfm.api.annotations.Write;

import java.util.Properties;

@FilesystemResource
public interface ComplexProxyResource {
    @Read
    @Path("props")
    Properties getProperties();

    @Write
    @Path("props")
    void setProperties(@Content Properties props);
}
