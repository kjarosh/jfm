package com.github.kjarosh.jfm.impl.mounter.rproxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Kamil Jarosz
 */
public class ReverseProxy {
    private Map<String, File> files = new HashMap<>();
    private List<String> directories = new ArrayList<>();
    private Map<Pattern, File> patternedFiles = new HashMap<>();

    public ReverseProxy(Class<?> resourceClass) {
        prepare(resourceClass);
    }

    private void prepare(Class<?> resourceClass) {
        for (Method method : resourceClass.getDeclaredMethods()) {
            reverseProxyMethod(method);
        }
    }

    private void reverseProxyMethod(Method method) {

    }

    private String toDirPath(String path) {
        return path.endsWith("/") ? path : path + '/';
    }

    public List<String> list(String path) {
        String dirPath = toDirPath(path);

        return files.keySet()
                .stream()
                .filter(p -> p.startsWith(dirPath))
                .map(p -> p.substring(dirPath.length()))
                .map(p -> p.substring(0, p.indexOf('/')))
                .collect(Collectors.toList());
    }

    public boolean isDirectory(String path) {
        String dirPath = toDirPath(path);
        return directories.contains(dirPath);
    }

    public boolean exists(String path) {
        return false;
    }

    public byte[] readFile(String path, long size, long offset) {
        return new byte[0];
    }
}
