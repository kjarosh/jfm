package com.github.kjarosh.jfm.impl.util;

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

    public static String getFirstName(String path) {
        return path.contains("/") ? path.substring(0, path.indexOf('/')) : path;
    }
}
