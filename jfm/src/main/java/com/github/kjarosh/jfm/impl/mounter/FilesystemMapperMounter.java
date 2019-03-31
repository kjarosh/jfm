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
    private final Path path;
    private Thread shutdownHook;
    private FuseFS fuseFs;

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

        this.fuseFs = new FilesystemMapperFuseStub(rp);
        this.fuseFs.mount(path, true, true);
        this.shutdownHook = new Thread(fuseFs::umount);

        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    public void umount() {
        Runtime.getRuntime().removeShutdownHook(shutdownHook);
        fuseFs.umount();
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
