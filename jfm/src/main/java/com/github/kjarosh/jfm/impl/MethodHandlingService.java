package com.github.kjarosh.jfm.impl;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.Delete;
import com.github.kjarosh.jfm.api.annotations.Listing;
import com.github.kjarosh.jfm.api.annotations.Read;
import com.github.kjarosh.jfm.api.annotations.Write;
import com.github.kjarosh.jfm.api.annotations.WriteBoolean;
import com.github.kjarosh.jfm.api.annotations.WriteBytes;
import com.github.kjarosh.jfm.api.annotations.WriteInteger;
import com.github.kjarosh.jfm.api.annotations.WriteString;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Kamil Jarosz
 */
public class MethodHandlingService {
    public <R> R handle(Method method, MethodHandler<R> invoker){
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
            return invoker.handleRead(read);
        } else if (write != null) {
            return invoker.handleWrite(write);
        } else if (writeBytes != null) {
            return invoker.handleWriteBytes(writeBytes);
        } else if (writeString != null) {
            return invoker.handleWriteString(writeString);
        } else if (writeBoolean != null) {
            return invoker.handleWriteBoolean(writeBoolean);
        } else if (writeInteger != null) {
            return invoker.handleWriteInteger(writeInteger);
        } else if (delete != null) {
            return invoker.handleDelete(delete);
        } else if (listing != null) {
            return invoker.handleListing(listing);
        }

        throw new FilesystemMapperException(
                "Method " + method.getName() + " is not annotated with any JFM annotation");
    }
}
