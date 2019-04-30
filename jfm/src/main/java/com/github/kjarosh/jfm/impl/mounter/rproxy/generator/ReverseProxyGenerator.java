package com.github.kjarosh.jfm.impl.mounter.rproxy.generator;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.impl.AnnotationHandlingService;
import com.github.kjarosh.jfm.impl.mounter.rproxy.vfs.VirtualDirectory;
import com.github.kjarosh.jfm.impl.mounter.rproxy.vfs.VirtualFile;
import com.github.kjarosh.jfm.impl.util.PathUtils;

import java.lang.reflect.Method;

/**
 * @author Kamil Jarosz
 */
public class ReverseProxyGenerator {
    private final AnnotationHandlingService annotationHandlingService = new AnnotationHandlingService();
    private final VirtualDirectory root;
    private final Class<?> resourceClass;
    private final Object resource;

    private ReverseProxyGenerator(Class<?> resourceClass, Object resource) {
        this.root = new VirtualDirectory();
        this.resourceClass = resourceClass;
        this.resource = resource;
    }

    public static VirtualDirectory generateVirtualFS(Class<?> resourceClass, Object resource) {
        ReverseProxyGenerator generator = new ReverseProxyGenerator(resourceClass, resource);
        generator.generateVfs();
        return generator.root;
    }

    private void generateVfs() {
        for (Method method : resourceClass.getDeclaredMethods()) {
            String path = ReverseProxyPathResolver.resolveFor(method);

            VirtualDirectory parentDirectory = getParentDirectory(method, path);
            String filename = PathUtils.getName(path);

            VirtualFile existingFile = parentDirectory.descend(filename).orElse(null);
            parentDirectory.addChild(filename, generateFile(method, existingFile));
        }
    }

    private VirtualDirectory getParentDirectory(Method method, String path) {
        return PathUtils.getParent(path)
                .map(parentPath -> root.ensureDirectoryCreated(parentPath)
                        .orElseThrow(() -> parentIsNotADirectoryException(method)))
                .orElse(root);
    }

    private FilesystemMapperException parentIsNotADirectoryException(Method method) {
        return new FilesystemMapperException("Invalid path for " + method
                + ", another method defines parent path as a file");
    }

    private VirtualFile generateFile(Method method, VirtualFile existingFile) {
        return annotationHandlingService.handle(method, new ReverseProxyFileGenerator(method, resource, existingFile));
    }
}
