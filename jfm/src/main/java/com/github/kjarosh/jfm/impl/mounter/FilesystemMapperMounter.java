package com.github.kjarosh.jfm.impl.mounter;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.impl.mounter.rproxy.ReverseProxy;
import ru.serce.jnrfuse.FuseFS;

import java.nio.file.Path;

/**
 * @author Kamil Jarosz
 */
public class FilesystemMapperMounter {
    private Path path;

    public FilesystemMapperMounter(Path path) {
        this.path = path;
    }

    public void mount(Object resource) {
        Class<?> resourceClass = getResourceClass(resource.getClass());
        if (resourceClass == null) {
            throw new FilesystemMapperException("Class " + resource.getClass() +
                    " does not represent a filesystem resource");
        }

        ReverseProxy rp = new ReverseProxy(resourceClass);
        FuseFS stub = new FilesystemMapperFuseStub(rp);

        stub.mount(path, true, true);
        Runtime.getRuntime().addShutdownHook(new Thread(stub::umount));
    }

    private Class<?> getResourceClass(Class<?> clazz) {
        if (clazz.isAnnotationPresent(FilesystemResource.class)) {
            return clazz;
        }

        if (clazz.getSuperclass() != null) {
            Class<?> parentResourceClass = getResourceClass(clazz.getSuperclass());
            if (parentResourceClass != null) {
                return parentResourceClass;
            }
        }

        for (Class<?> anInterface : clazz.getInterfaces()) {
            Class<?> interfaceResourceClass = getResourceClass(anInterface);
            if (interfaceResourceClass != null) {
                return interfaceResourceClass;
            }
        }

        return null;
    }
}
