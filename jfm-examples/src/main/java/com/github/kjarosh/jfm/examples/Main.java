package com.github.kjarosh.jfm.examples;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.FilesystemMapperTarget;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.exit(-1);
        }

        Path rootPath = Paths.get(args[0]);
        setupFilesystem(rootPath);

        FilesystemMapperTarget target = FilesystemMapper.instance().getTarget(rootPath);
        PersonInfoResource resource = target.proxy(PersonInfoResource.class);

        System.out.println(resource.getFirstName());
        System.out.println(resource.getLastName());
        System.out.println(resource.getName());
    }

    private static void setupFilesystem(Path path) throws IOException {
        Files.createDirectories(path.resolve("kamil"));
        Files.write(path.resolve("kamil/first-name"), "Kamil".getBytes());
        Files.write(path.resolve("kamil/last-name"), "Jarosz".getBytes());
    }
}
