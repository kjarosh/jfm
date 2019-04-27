package com.github.kjarosh.jfm.tests.listing;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.tests.JfmMountTestBase;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ListingMountTest extends JfmMountTestBase {
    private FilesystemMapper fm = FilesystemMapper.instance();

    @Mock
    private ListingMountResource listingMountResource;

    @BeforeEach
    void setUp() {
        fm.getTarget(root).mount(listingMountResource);
    }

    @AfterEach
    void tearDown() {
        fm.getTarget(root).umountAll();
    }

    @Test
    void testList() {
        String[] listing = {"a", "b", "c", "f"};

        when(listingMountResource.list())
                .thenReturn(Arrays.asList(listing));

        assertThat(list(root.resolve("list")))
                .containsExactlyInAnyOrder(listing);
    }

    @Test
    void testListAsDummyMap() {
        when(listingMountResource.listRootAsDummyMap())
                .thenReturn(ImmutableMap.of(
                        "dummy1", Mockito.mock(DummyResource.class),
                        "dummy2", Mockito.mock(DummyResource.class),
                        "dummy3", Mockito.mock(DummyResource.class)));

        String[] expectedListing = {"dummy1", "dummy2", "dummy3"};

        assertThat(list(root.resolve("list-as-map")))
                .containsExactlyInAnyOrder(expectedListing);
    }
}
