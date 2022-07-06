package com.github.kjarosh.jfm.tests.constants;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.tests.JfmMountTestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Kamil Jarosz
 */
class ConstantsMountTest extends JfmMountTestBase {
    private final FilesystemMapper fm = FilesystemMapper.instance();

    @Mock
    private ConstantsMountResource constantsMountResource;

    @BeforeEach
    void setUp() {
        fm.getTarget(root).mount(constantsMountResource);
    }

    @AfterEach
    void tearDown() {
        fm.getTarget(root).umountAll();
    }

    @Test
    void testSetConstantBytes() {
        write(root.resolve("constant-bytes"), "asdf");

        verify(constantsMountResource, times(1))
                .setConstantBytes();
    }

    @Test
    void testSetConstantInt() {
        write(root.resolve("constant-int"), "7");

        verify(constantsMountResource, times(1))
                .setConstantInt();
    }

    @Test
    void testSetConstantBoolean() {
        write(root.resolve("constant-boolean"), "true");

        verify(constantsMountResource, times(1))
                .setConstantBoolean();
    }

    @Test
    void testSetConstantString() {
        write(root.resolve("constant-string"), "asdf");

        verify(constantsMountResource, times(1))
                .setConstantString();
    }
}
