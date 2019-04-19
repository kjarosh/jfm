package com.github.kjarosh.jfm.tests.basic;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.tests.JfmMountTestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.OptionalInt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    @Test
    void testOptionalName() {
        when(basicResource.getOptionalName()).thenReturn(Optional.of("sample name"));

        assertThat(read(root.resolve("optional-name")))
                .isEqualTo("sample name");
    }

    @Test
    void testOptionalEmpty() {
        when(basicResource.getOptionalName()).thenReturn(Optional.empty());

        assertThatThrownBy(() -> read(root.resolve("optional-name")))
                .hasCauseInstanceOf(NoSuchFileException.class);
    }

    @Test
    void testInt() {
        when(basicResource.getInt()).thenReturn(4321);

        assertThat(read(root.resolve("number")))
                .isEqualTo("4321");
    }

    @Test
    void testOptionalInt() {
        when(basicResource.getOptionalInt()).thenReturn(OptionalInt.of(4321));

        assertThat(read(root.resolve("optional-number")))
                .isEqualTo("4321");
    }

    @Test
    void testSetOptionalInt() {
        write(root.resolve("optional-number"), "4321");

        OptionalInt wantedArgument = OptionalInt.of(4321);
        verify(basicResource, times(1))
                .setOptionalInt(wantedArgument);
        verify(basicResource, never())
                .setOptionalInt(not(eq(wantedArgument)));
    }

    @Test
    void testRemoveOptionalInt() {
        remove(root.resolve("optional-number"));

        verify(basicResource, times(1))
                .removeOptionalInt();
    }

    /*@Test
    void testInteger() {
        when(basicResource.getInteger()).thenReturn(4321);

        write(root.resolve("invalid-integer"), "not integer");
    }

    /*@Test
    void testInvalidNumber() {
        assertThatThrownBy(basicResource::getInvalidInteger)
                .isInstanceOf(TypeHandlingException.class)
                .hasCauseInstanceOf(NumberFormatException.class);
    }

    @Test
    void testByte() {
        assertThat(basicResource.getByte())
                .isEqualTo((byte) 'a');
    }

    @Test
    void testSetByte() {
        basicResource.setByte((byte) -7);
        assertThat(basicResource.getByte())
                .isEqualTo((byte) -7);
    }

    @Test
    void testChar() {
        assertThat(basicResource.getChar())
                .isEqualTo('\u0105');
    }

    @Test
    void testSetChar() {
        basicResource.setChar('\u0106');
        assertThat(basicResource.getChar())
                .isEqualTo('\u0106');
    }

    @Test
    void testFloat() {
        assertThat(basicResource.getFloat())
                .isEqualTo(12.34f);
    }

    @Test
    void testSetFloat() {
        basicResource.setFloat(43.21f);
        assertThat(basicResource.getFloat())
                .isEqualTo(43.21f);
    }

    @Test
    void testDouble() {
        assertThat(basicResource.getDouble())
                .isEqualTo(12.34d);
    }

    @Test
    void testSetDouble() {
        basicResource.setDouble(43.21d);
        assertThat(basicResource.getDouble())
                .isEqualTo(43.21d);
    }*/
}
