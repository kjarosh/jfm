package com.github.kjarosh.jfm.impl.mounter.rproxy;

import com.github.kjarosh.jfm.impl.util.PathUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Kamil Jarosz
 */
public class ReverseProxy {
    private Map<String, ReverseProxyFileHandler> fileHandlers = new HashMap<>();
    private Object resource;

    public ReverseProxy(Class<?> resourceClass, Object resource) {
        this.resource = resource;
        prepareHandlers(resourceClass);
    }

    private void prepareHandlers(Class<?> resourceClass) {
        for (Method method : resourceClass.getDeclaredMethods()) {
            String path = new ReverseProxyPathResolver(method).resolve();
            ReverseProxyFileHandler handler = assertCreated(path);
            handler.addHandlingMethod(method);
        }
    }

    private ReverseProxyFileHandler assertCreated(String path) {
        if (!fileHandlers.containsKey(path)) {
            fileHandlers.put(path, new ReverseProxyFileHandler(path, getResource()));
        }

        return getFileHandler(path);
    }

    private ReverseProxyFileHandler getFileHandler(String path) {
        return fileHandlers.get(path);
    }

    public List<String> list(String path) {
        String dirPath = PathUtils.toDirPath(path);

        return fileHandlers.keySet()
                .stream()
                .filter(p -> p.startsWith(dirPath))
                .map(p -> p.substring(dirPath.length()))
                .map(PathUtils::getFirstName)
                .collect(Collectors.toList());
    }

    public boolean isDirectory(String path) {
        String dirPath = PathUtils.toDirPath(path);
        return fileHandlers.keySet()
                .stream()
                .anyMatch(f -> f.startsWith(dirPath));
    }

    public boolean exists(String path) {
        return fileHandlers.containsKey(path);
    }

    public byte[] readFile(String path) {
        return getFileHandler(path).read();
    }

    public void writeFile(String path, byte[] data) {
        getFileHandler(path).write(data);
    }

    public void deleteFile(String path) {
        getFileHandler(path).delete();
    }

    public Object getResource() {
        return resource;
    }
}
