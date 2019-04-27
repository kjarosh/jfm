package com.github.kjarosh.jfm.tests.listing;

import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Listing;
import com.github.kjarosh.jfm.api.annotations.Path;

import java.util.List;
import java.util.Map;

@FilesystemResource
public interface ListingMountResource {
    @Listing
    @Path("list")
    List<String> list();

    @Listing
    @Path("list-as-map")
    Map<String, DummyResource> listRootAsDummyMap();
}
