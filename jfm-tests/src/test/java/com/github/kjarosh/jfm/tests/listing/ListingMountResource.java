package com.github.kjarosh.jfm.tests.listing;

import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Listing;
import com.github.kjarosh.jfm.api.annotations.Path;

import java.util.List;

@FilesystemResource
interface ListingMountResource {
    @Listing
    @Path("list")
    List<String> list();
}
