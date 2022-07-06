package com.github.kjarosh.jfm.tests;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

class DeletingFileVisitor extends SimpleFileVisitor<Path> {
    private final Path root;

    DeletingFileVisitor(Path root) {
        this.root = root;
    }

    @Override
    public FileVisitResult postVisitDirectory(
            Path dir, IOException exc) throws IOException {
        if (root == null || !root.equals(dir)) {
            Files.deleteIfExists(dir);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(
            Path file, BasicFileAttributes attrs)
            throws IOException {
        Files.deleteIfExists(file);
        return FileVisitResult.CONTINUE;
    }
}
