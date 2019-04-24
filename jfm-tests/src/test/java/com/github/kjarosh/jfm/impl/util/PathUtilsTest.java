package com.github.kjarosh.jfm.impl.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kamil Jarosz
 */
class PathUtilsTest {

    @Test
    void toDirPath() {
        assertThat(PathUtils.toDirPath("a"))
                .isEqualTo("a/");
        assertThat(PathUtils.toDirPath("a/"))
                .isEqualTo("a/");
        assertThat(PathUtils.toDirPath("a/b"))
                .isEqualTo("a/b/");
        assertThat(PathUtils.toDirPath("a/b/"))
                .isEqualTo("a/b/");
    }

    @Test
    void toFilePath() {
        assertThat(PathUtils.toFilePath("a"))
                .isEqualTo("a");
        assertThat(PathUtils.toFilePath("a/"))
                .isEqualTo("a");
        assertThat(PathUtils.toFilePath("a/b"))
                .isEqualTo("a/b");
        assertThat(PathUtils.toFilePath("a/b/"))
                .isEqualTo("a/b");
    }

    @Test
    void getName() {
        assertThat(PathUtils.getName("a"))
                .isEqualTo("a");
        assertThat(PathUtils.getName("a/"))
                .isEqualTo("a");
        assertThat(PathUtils.getName("a/b"))
                .isEqualTo("b");
        assertThat(PathUtils.getName("a/b/"))
                .isEqualTo("b");
        assertThat(PathUtils.getName("a/b/c"))
                .isEqualTo("c");
    }

    @Test
    void getFirstName() {
        assertThat(PathUtils.getFirstName("a"))
                .isEqualTo("a");
        assertThat(PathUtils.getFirstName("a/"))
                .isEqualTo("a");
        assertThat(PathUtils.getFirstName("a/b"))
                .isEqualTo("a");
        assertThat(PathUtils.getFirstName("a/b/"))
                .isEqualTo("a");
        assertThat(PathUtils.getFirstName("a/b/c"))
                .isEqualTo("a");
    }

    @Test
    void shift() {
        assertThat(PathUtils.shift("a"))
                .isNotPresent();
        assertThat(PathUtils.shift("a/"))
                .isNotPresent();
        assertThat(PathUtils.shift("a/b"))
                .contains("b");
        assertThat(PathUtils.shift("a/b/"))
                .contains("b/");
        assertThat(PathUtils.shift("a/b/c"))
                .contains("b/c");
        assertThat(PathUtils.shift("a/b/c/"))
                .contains("b/c/");
    }

    @Test
    void getParent() {
        assertThat(PathUtils.getParent("a"))
                .isNotPresent();
        assertThat(PathUtils.getParent("a/"))
                .isNotPresent();
        assertThat(PathUtils.getParent("a/b"))
                .contains("a");
        assertThat(PathUtils.getParent("a/b/"))
                .contains("a");
    }
}
