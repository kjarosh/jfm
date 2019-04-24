package com.github.kjarosh.jfm.impl.mounter.rproxy;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.impl.mounter.rproxy.generator.ReverseProxyGenerator;
import com.github.kjarosh.jfm.impl.mounter.rproxy.vfs.VirtualDirectory;

import java.util.List;

/**
 * @author Kamil Jarosz
 */
public class ReverseProxy {
    private VirtualDirectory root;
    private Object resource;

    public ReverseProxy(Class<?> resourceClass, Object resource) {
        this.resource = resource;
        this.root = ReverseProxyGenerator.generateVirtualFS(resourceClass, resource);
    }

    public boolean isDirectory(String path) {
        return root.resolve(path)
                .map(f -> f instanceof VirtualDirectory)
                .orElse(false);
    }

    public boolean exists(String path) {
        return root.resolve(path).isPresent();
    }

    public byte[] readFile(String path) {
        return root.resolve(path)
                .orElseThrow(() -> new FilesystemMapperException("Unknown path: " + path))
                .read();
    }

    public void writeFile(String path, byte[] data) {
        root.resolve(path)
                .orElseThrow(() -> new FilesystemMapperException("Unknown path: " + path))
                .write(data);
    }

    public void deleteFile(String path) {
        root.resolve(path)
                .orElseThrow(() -> new FilesystemMapperException("Unknown path: " + path))
                .delete();
    }

    public List<String> list(String path) {
        return root.resolve(path)
                .orElseThrow(() -> new FilesystemMapperException("Unknown path: " + path))
                .list();
    }

    public Object getResource() {
        return resource;
    }
}
