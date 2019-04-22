package com.github.kjarosh.jfm.tests.listing;

import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Listing;

import java.util.List;
import java.util.stream.Stream;

@FilesystemResource
interface ListingProxyResource {
    @Listing
    List<String> listRoot();

    @Listing
    Stream<String> streamRoot();
}
