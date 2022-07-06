package com.github.kjarosh.jfm.tests.methodresolving;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.tests.JfmMountTestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MethodResolvingMountTest extends JfmMountTestBase {
    private final FilesystemMapper fm = FilesystemMapper.instance();

    @Mock
    private MethodResolvingMountResource methodResolvingMountResource;

    @BeforeEach
    void setUp() {
        fm.getTarget(root).mount(methodResolvingMountResource);
    }

    @AfterEach
    void tearDown() {
        fm.getTarget(root).umountAll();
    }

    // TODO @Test
    void testResolvingSetter() {
        write(root.resolve("value"), "string");

        verify(methodResolvingMountResource, times(1))
                .setString("string");
        verify(methodResolvingMountResource, times(0))
                .setInteger(any());
    }
}
