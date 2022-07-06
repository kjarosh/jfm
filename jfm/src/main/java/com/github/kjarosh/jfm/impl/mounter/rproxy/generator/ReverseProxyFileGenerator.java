package com.github.kjarosh.jfm.impl.mounter.rproxy.generator;

import com.github.kjarosh.jfm.api.annotations.Delete;
import com.github.kjarosh.jfm.api.annotations.Listing;
import com.github.kjarosh.jfm.api.annotations.Read;
import com.github.kjarosh.jfm.api.annotations.Write;
import com.github.kjarosh.jfm.api.annotations.WriteBoolean;
import com.github.kjarosh.jfm.api.annotations.WriteBytes;
import com.github.kjarosh.jfm.api.annotations.WriteInteger;
import com.github.kjarosh.jfm.api.annotations.WriteString;
import com.github.kjarosh.jfm.impl.annotation.AnnotationHandler;
import com.github.kjarosh.jfm.impl.annotation.AnnotationHandlingService;
import com.github.kjarosh.jfm.impl.mounter.rproxy.vfs.VirtualDirectory;
import com.github.kjarosh.jfm.impl.mounter.rproxy.vfs.VirtualFile;
import com.github.kjarosh.jfm.impl.mounter.rproxy.vfs.VirtualRegularFile;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author Kamil Jarosz
 */
class ReverseProxyFileGenerator implements AnnotationHandler<VirtualFile> {
    private final AnnotationHandlingService annotationHandlingService = new AnnotationHandlingService();
    private final Object resource;
    private final Method method;
    private final VirtualFile existingFile;

    ReverseProxyFileGenerator(Method method, Object resource, VirtualFile existingFile) {
        this.method = method;
        this.resource = resource;
        this.existingFile = existingFile;
    }

    private VirtualRegularFile ensureRegularFile() {
        if (existingFile != null) {
            if (existingFile instanceof VirtualRegularFile) {
                return (VirtualRegularFile) existingFile;
            } else {
                throw new ReverseProxyGenerationException("Invalid file type for: " + method);
            }
        }

        return new VirtualRegularFile();
    }

    private VirtualDirectory ensureDirectory() {
        if (existingFile != null) {
            if (existingFile instanceof VirtualDirectory) {
                return (VirtualDirectory) existingFile;
            } else {
                throw new ReverseProxyGenerationException("Invalid file type for: " + method);
            }
        }

        return new VirtualDirectory();
    }

    @Override
    public VirtualFile handleRead(Read read) {
        return addReadHandler();
    }

    @Override
    public VirtualFile handleWrite(Write write) {
        return addWriteHandler();
    }

    @Override
    public VirtualFile handleWriteBytes(WriteBytes writeBytes) {
        return addWriteHandler();
    }

    @Override
    public VirtualFile handleWriteString(WriteString writeString) {
        return addWriteHandler();
    }

    @Override
    public VirtualFile handleWriteBoolean(WriteBoolean writeBoolean) {
        return addWriteHandler();
    }

    @Override
    public VirtualFile handleWriteInteger(WriteInteger writeInteger) {
        return addWriteHandler();
    }

    @Override
    public VirtualFile handleDelete(Delete delete) {
        return addDeleteHandler();
    }

    @Override
    public VirtualFile handleListing(Listing listing) {
        return addListingHandler();
    }

    private VirtualFile addReadHandler() {
        VirtualRegularFile file = ensureRegularFile();
        file.setReadHandler(this::handleRead);
        return file;
    }

    private VirtualFile addWriteHandler() {
        VirtualRegularFile file = ensureRegularFile();
        file.setWriteHandler(this::handleWrite);
        return file;
    }

    private VirtualFile addDeleteHandler() {
        VirtualRegularFile file = ensureRegularFile();
        file.setDeleteHandler(this::handleDelete);
        return file;
    }

    private VirtualFile addListingHandler() {
        VirtualDirectory file = ensureDirectory();
        file.setListingHandler(this::handleListing);
        return file;
    }

    private byte[] handleRead() {
        return handle(ReverseProxyReadHandler::new);
    }

    private void handleWrite(byte[] data) {
        handle((method, resource) -> new ReverseProxyWriteHandler(method, resource, data));
    }

    private void handleDelete() {
        handle(ReverseProxyDeleteHandler::new);
    }

    private List<String> handleListing() {
        return handle(ReverseProxyListingHandler::new);
    }

    private <T> T handle(BiFunction<Method, Object, AnnotationHandler<T>> handler) {
        return annotationHandlingService.handle(method, handler.apply(method, resource));
    }
}
