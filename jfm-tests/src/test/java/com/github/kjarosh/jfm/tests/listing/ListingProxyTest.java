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

    @Test
    void testPersonListing() {
        write(root.resolve("dummy1/test"), "1");
        write(root.resolve("dummy2/test"), "2");
        write(root.resolve("dummy3/test"), "3");

        String[] expectedTestValues = {"1", "2", "3"};

        assertThat(listingProxyResource.listRootAsDummy())
                .extracting(DummyResource::getTestValue)
                .containsExactlyInAnyOrder(expectedTestValues);
        assertThat(listingProxyResource.streamRootAsDummy())
                .extracting(DummyResource::getTestValue)
                .containsExactlyInAnyOrder(expectedTestValues);
    }

    @Test
    void testPersonMapListing() {
        write(root.resolve("dummy1/test"), "1");
        write(root.resolve("dummy2/test"), "2");
        write(root.resolve("dummy3/test"), "3");

        String[] expectedNames = {"dummy1", "dummy2", "dummy3"};
        String[] expectedTestValues = {"1", "2", "3"};

        assertThat(listingProxyResource.listRootAsDummyMap())
                .containsOnlyKeys(expectedNames)
                .extractingFromEntries(e -> e.getValue().getTestValue())
                .containsExactlyInAnyOrder(expectedTestValues);
    }
}
