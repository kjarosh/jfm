package com.github.kjarosh.jfm.impl.proxy;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.Delete;
import com.github.kjarosh.jfm.api.annotations.FilesystemResource;
import com.github.kjarosh.jfm.api.annotations.Listing;
import com.github.kjarosh.jfm.api.annotations.Read;
import com.github.kjarosh.jfm.api.annotations.Write;
import com.github.kjarosh.jfm.api.annotations.WriteBoolean;
import com.github.kjarosh.jfm.api.annotations.WriteBytes;
import com.github.kjarosh.jfm.api.annotations.WriteInteger;
import com.github.kjarosh.jfm.api.annotations.WriteString;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Kamil Jarosz
 */
public class FilesystemMapperProxyHandler<T> implements InvocationHandler {
    private final Class<T> resourceClass;
    private final Path path;

    public FilesystemMapperProxyHandler(Class<T> resourceClass, Path path) {
        this.resourceClass = resourceClass;
        this.path = path;

        FilesystemResource resourceAnnotation = resourceClass
                .getAnnotation(FilesystemResource.class);

        if (resourceAnnotation == null) {
            throw new FilesystemMapperException("Resource class is not annotated with " +
                    "@" + FilesystemResource.class.getSimpleName());
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        InvokeContext ic = new InvokeContext(path, method, args);
        FilesystemMapperMethodInvoker invoker = new FilesystemMapperMethodInvoker(ic);

        Read read = method.getAnnotation(Read.class);
        Write write = method.getAnnotation(Write.class);
        WriteString writeString = method.getAnnotation(WriteString.class);
        WriteBytes writeBytes = method.getAnnotation(WriteBytes.class);
        WriteBoolean writeBoolean = method.getAnnotation(WriteBoolean.class);
        WriteInteger writeInteger = method.getAnnotation(WriteInteger.class);
        Delete delete = method.getAnnotation(Delete.class);
        Listing listing = method.getAnnotation(Listing.class);

        long annotationsCount = Stream.of(read, write, writeBoolean,
                writeBytes, writeString, delete, writeInteger, listing)
                .filter(Objects::nonNull)
                .count();

        if (annotationsCount > 1) {
            throw new FilesystemMapperException(
                    "Method " + method.getName() + " is not properly annotated");
        } else if (read != null) {
            return invoker.invokeRead(read);
        } else if (write != null) {
            invoker.invokeWrite(write);
            return null;
        } else if (writeBytes != null) {
            invoker.invokeWriteBytes(writeBytes);
            return null;
        } else if (writeString != null) {
            invoker.invokeWriteString(writeString);
            return null;
        } else if (writeBoolean != null) {
            invoker.invokeWriteBoolean(writeBoolean);
            return null;
        } else if (writeInteger != null) {
            invoker.invokeWriteInteger(writeInteger);
            return null;
        } else if (delete != null) {
            invoker.invokeDelete(delete);
            return null;
        } else if (listing != null) {
            return invoker.invokeListing(listing);
        }

        throw new FilesystemMapperException(
                "Method " + method.getName() + " is not annotated with any JFM annotation");
    }
}
