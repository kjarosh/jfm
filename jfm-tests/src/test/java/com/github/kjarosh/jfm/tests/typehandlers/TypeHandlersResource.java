package com.github.kjarosh.jfm.tests.typehandlers;

import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Path;
import com.github.kjarosh.jfm.api.annotations.Read;

import java.util.List;

@FilesystemResource
public interface TypeHandlersResource {
    @Read
    @Path("text")
    Text getText();

    @Read
    @Path("list")
    List<String> getStringList();

    @Read
    @Path("list")
    List<Text> getTextList();
}
