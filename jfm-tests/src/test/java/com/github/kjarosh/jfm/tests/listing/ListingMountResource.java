package com.github.kjarosh.jfm.tests.listing;

import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Listing;
import com.github.kjarosh.jfm.api.annotations.Path;

import java.util.List;
import java.util.stream.Stream;

@FilesystemResource
interface ListingMountResource {
    @Listing
    @Path("list")
    List<String> list();

    @Listing
    @Path("stream")
    Stream<String> stream();
}
