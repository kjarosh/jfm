package com.github.kjarosh.jfm.tests.listing;

import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Path;
import com.github.kjarosh.jfm.api.annotations.Read;

/**
 * @author Kamil Jarosz
 */
@FilesystemResource
public interface DummyResource {
    @Read
    @Path("test")
    String getTestValue();
}
