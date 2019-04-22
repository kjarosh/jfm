package com.github.kjarosh.jfm.tests.listing;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.tests.JfmProxyTestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ListingMountTest extends JfmProxyTestBase {
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
}
