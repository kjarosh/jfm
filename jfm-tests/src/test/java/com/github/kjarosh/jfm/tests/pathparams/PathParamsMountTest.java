package com.github.kjarosh.jfm.tests.pathparams;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.tests.JfmMountTestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class PathParamsMountTest extends JfmMountTestBase {
    private FilesystemMapper fm = FilesystemMapper.instance();

    @Mock
    private PathParamsMountResource pathParamsMountResource;

    @BeforeEach
    void setUp() {
        fm.getTarget(root).mount(pathParamsMountResource);
    }

    @AfterEach
    void tearDown() {
        fm.getTarget(root).umountAll();
    }

    @Test
    @Disabled
    void testInteger() {
        when(pathParamsMountResource.getInteger("something"))
                .thenReturn(1234);

        assertThat(read(root.resolve("something")))
                .isEqualTo("1234");
    }
}
