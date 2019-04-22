package com.github.kjarosh.jfm.tests.pathparams;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.tests.JfmProxyTestBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PathParamsProxyTest extends JfmProxyTestBase {
    private final PathParamsResource pathParamsResource;

    PathParamsProxyTest() {
        this.pathParamsResource = FilesystemMapper.instance()
                .getTarget(super.getRoot())
                .proxy(PathParamsResource.class);
    }

    @Test
    void testInteger() {
        write(root.resolve("first"), "1");
        write(root.resolve("second"), "2");
        write(root.resolve("third"), "3");

        assertThat(pathParamsResource.getInteger("first"))
                .isEqualTo(1);
        assertThat(pathParamsResource.getInteger("second"))
                .isEqualTo(2);
        assertThat(pathParamsResource.getInteger("third"))
                .isEqualTo(3);
    }

    @Test
    void testInnerInteger() {
        write(root.resolve("first.d/int"), "1");
        write(root.resolve("second.d/int"), "2");
        write(root.resolve("third.d/int"), "3");

        assertThat(pathParamsResource.getInnerInteger("first.d"))
                .isEqualTo(1);
        assertThat(pathParamsResource.getInnerInteger("second.d"))
                .isEqualTo(2);
        assertThat(pathParamsResource.getInnerInteger("third.d"))
                .isEqualTo(3);
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

        assertThat(pathParamsResource.listNames())
                .containsExactlyInAnyOrder(expectedListing);
        assertThat(pathParamsResource.streamNames())
                .containsExactlyInAnyOrder(expectedListing);
    }
}
