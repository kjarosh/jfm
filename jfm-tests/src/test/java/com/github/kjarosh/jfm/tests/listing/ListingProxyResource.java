package com.github.kjarosh.jfm.tests.listing;

import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Listing;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@FilesystemResource
public interface ListingProxyResource {
    @Listing
    List<String> listRoot();

    @Listing
    Stream<String> streamRoot();

    @Listing
    List<DummyResource> listRootAsDummy();

    @Listing
    Stream<DummyResource> streamRootAsDummy();

    @Listing
    Map<String, DummyResource> listRootAsDummyMap();
}
