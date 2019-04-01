package com.github.kjarosh.jfm.impl;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.FilesystemMapperTarget;
import com.github.kjarosh.jfm.impl.mounter.FilesystemMapperMounter;
import com.github.kjarosh.jfm.impl.proxy.ResourceMethodInvocationHandler;

import java.lang.reflect.Proxy;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Kamil Jarosz
 */
public class FilesystemMapperTargetImpl implements FilesystemMapperTarget {
    private final Map<Object, FilesystemMapperMounter> mounters = new HashMap<>(1);
    private final Path path;

    FilesystemMapperTargetImpl(Path path) {
        this.path = path;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T proxy(Class<T> resourceClass) {
        return (T) Proxy.newProxyInstance(resourceClass.getClassLoader(),
                new Class[]{resourceClass},
                new ResourceMethodInvocationHandler(resourceClass, path));
    }

    @Override
    public void mount(Object resource) {
        if (mounters.containsKey(resource)) {
            throw new FilesystemMapperException(
                    "The resource " + resource + " has already been mounted here");
        }

        FilesystemMapperMounter mounter = new FilesystemMapperMounter(path);
        mounters.put(resource, mounter);
        mounter.mount(resource);
    }

    @Override
    public void umount(Object resource) {
        if (!mounters.containsKey(resource)) {
            throw new FilesystemMapperException(
                    "The resource " + resource + " has not been mounted here");
        }

        mounters.get(resource).umount();
        mounters.remove(resource);
    }

    @Override
    public void umountAll() {
        Set<Object> keys = new LinkedHashSet<>(mounters.keySet());
        keys.forEach(key -> {
            mounters.get(key).umount();
            mounters.remove(key);
        });
    }
}
