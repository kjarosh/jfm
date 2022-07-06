package com.github.kjarosh.jfm.impl.mounter;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.impl.FilesystemMapperProperties;
import com.github.kjarosh.jfm.impl.mounter.rproxy.ReverseProxy;
import ru.serce.jnrfuse.FuseFS;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.ExecutorService;

/**
 * @author Kamil Jarosz
 */
public class FilesystemMapperMounter {
    private static final String[] MOUNT_OPTS = {"-o", "direct_io", "-o", "auto_unmount"};

    private final ExecutorService executorService;
    private final Path path;
    private Thread shutdownHook;
    private FuseFS fuseFs;

    FilesystemMapperMounter(ExecutorService executorService, Path path) {
        if (!Files.exists(path)) {
            throw new IllegalStateException("The path " + path + " doesn't exist");
        }

        this.executorService = executorService;
        this.path = path;
    }

    public void mount(Object resource) {
        Class<?> resourceClass = getResourceClass(resource.getClass());
        if (resourceClass == null) {
            throw new FilesystemMapperException("Class " + resource.getClass() +
                    " does not represent a filesystem resource");
        }

        ReverseProxy rp = new ReverseProxy(resourceClass, resource);

        this.fuseFs = createFs(rp);
        this.fuseFs.mount(path, false, FilesystemMapperProperties.mountInDebugMode(), MOUNT_OPTS);
        this.shutdownHook = new Thread(fuseFs::umount);

        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    private ThreadDelegatingFS createFs(ReverseProxy rp) {
        return new ThreadDelegatingFS(executorService,
                new ErrorCodeExceptionHandlingFS(new FilesystemMapperFS(rp)),
                Duration.ofSeconds(10));
    }

    public void umount() {
        fuseFs.umount();
        if (shutdownHook != null) {
            Runtime.getRuntime().removeShutdownHook(shutdownHook);
        }
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
