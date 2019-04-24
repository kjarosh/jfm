package com.github.kjarosh.jfm.tests.listing;

import com.github.kjarosh.jfm.tests.JfmProxyTestBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ListingProxyTest extends JfmProxyTestBase {
    private final ListingProxyResource listingProxyResource;

    ListingProxyTest() {
        this.listingProxyResource = proxy(ListingProxyResource.class);
    }

    @Test
    void testListing() {
        write(root.resolve("first"), "");
        write(root.resolve("second"), "");
        write(root.resolve("third"), "");
        write(root.resolve("first.d/int"), "");
        write(root.resolve("second.d/int"), "");
        write(root.resolve("third.d/int"), "");

        String[] expectedListing = {"first", "second", "third",
                "first.d", "second.d", "third.d"};

        assertThat(listingProxyResource.listRoot())
                .containsExactlyInAnyOrder(expectedListing);
        assertThat(listingProxyResource.streamRoot())
                .containsExactlyInAnyOrder(expectedListing);
    }
}
