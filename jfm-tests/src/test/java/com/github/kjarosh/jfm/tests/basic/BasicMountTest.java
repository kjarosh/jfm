package com.github.kjarosh.jfm.tests.basic;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.tests.JfmMountTestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Kamil Jarosz
 */
public class BasicMountTest extends JfmMountTestBase {
    private FilesystemMapper fm = FilesystemMapper.instance();
    private final Path root = getRoot();

    @Mock
    private BasicResource basicResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        fm.getTarget(root).mount(basicResource);
    }

    @AfterEach
    void tearDown() {
        fm.getTarget(root).umountAll();
    }

    @Test
    void testName() {
        when(basicResource.getName()).thenReturn("test name");

        assertThat(read(root.resolve("name")))
                .isEqualTo("test name");
    }
}
