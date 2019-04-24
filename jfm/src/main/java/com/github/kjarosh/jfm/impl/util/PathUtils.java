package com.github.kjarosh.jfm.impl.util;

import java.util.Optional;

/**
 * @author Kamil Jarosz
 */
public class PathUtils {
    public static String toDirPath(String path) {
        return path.endsWith("/") ? path : path + '/';
    }

    public static String toFilePath(String path) {
        return path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
    }

    public static String getName(String path) {
        path = toFilePath(path);
        return path.contains("/") ? path.substring(path.lastIndexOf('/') + 1) : path;
    }

    public static String getFirstName(String path) {
        return path.contains("/") ? path.substring(0, path.indexOf('/')) : path;
    }

    public static Optional<String> shift(String path) {
        if (!path.contains("/")) {
            return Optional.empty();
        }

        return Optional.of(path.substring(path.indexOf("/") + 1))
                .filter(s -> !s.isEmpty());
    }

    public static Optional<String> getParent(String path) {
        path = toFilePath(path);

        if (path.contains("/")) {
            return Optional.of(path.substring(0, path.lastIndexOf('/')));
        } else {
            return Optional.empty();
        }
    }
}
