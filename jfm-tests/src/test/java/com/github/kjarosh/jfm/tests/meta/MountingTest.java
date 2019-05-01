package com.github.kjarosh.jfm.tests.meta;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.tests.JfmTestBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Kamil Jarosz
 */
class MountingTest extends JfmTestBase {
    private FilesystemMapper fm = FilesystemMapper.instance();

    @Test
    void testMountUnmount() {
        DummyResource resource = new DummyResource() {
        };

        fm.getTarget(root).mount(resource);
        fm.getTarget(root).umount(resource);
    }

    @Test
    void testMountUnmountAll() {
        DummyResource resource = new DummyResource() {
        };

        fm.getTarget(root).mount(resource);
        fm.getTarget(root).umountAll();
    }

    @Test
    void testUnmountInvalid() {
        DummyResource resource = new DummyResource() {
        };

        assertThatThrownBy(() -> fm.getTarget(root).umount(resource))
                .isInstanceOf(FilesystemMapperException.class)
                .hasMessageContaining("has not been mounted");
    }

    @Test
    void testDoubleMount() {
        DummyResource resource = new DummyResource() {
        };

        fm.getTarget(root).mount(resource);

        assertThatThrownBy(() -> fm.getTarget(root).mount(resource))
                .isInstanceOf(FilesystemMapperException.class)
                .hasMessageContaining("has already been mounted");
    }

    @Test
    void testMountInvalid() {
        assertThatThrownBy(() -> fm.getTarget(root).mount(new Object()))
                .isInstanceOf(FilesystemMapperException.class)
                .hasMessageContaining("does not represent a filesystem resource");
    }
}