package com.github.kjarosh.jfm.impl.annotation;

import com.github.kjarosh.jfm.api.annotations.Delete;
import com.github.kjarosh.jfm.api.annotations.Listing;
import com.github.kjarosh.jfm.api.annotations.Read;
import com.github.kjarosh.jfm.api.annotations.Write;
import com.github.kjarosh.jfm.api.annotations.WriteBoolean;
import com.github.kjarosh.jfm.api.annotations.WriteBytes;
import com.github.kjarosh.jfm.api.annotations.WriteInteger;
import com.github.kjarosh.jfm.api.annotations.WriteString;
import com.github.kjarosh.jfm.impl.UnsupportedAnnotationException;

import java.lang.annotation.Annotation;

/**
 * @author Kamil Jarosz
 */
public interface AnnotationHandler<T> {
    default T handle(Annotation annotation) {
        if (annotation instanceof Read) {
            return handleRead((Read) annotation);
        } else if (annotation instanceof Write) {
            return handleWrite((Write) annotation);
        } else if (annotation instanceof WriteBytes) {
            return handleWriteBytes((WriteBytes) annotation);
        } else if (annotation instanceof WriteString) {
            return handleWriteString((WriteString) annotation);
        } else if (annotation instanceof WriteBoolean) {
            return handleWriteBoolean((WriteBoolean) annotation);
        } else if (annotation instanceof WriteInteger) {
            return handleWriteInteger((WriteInteger) annotation);
        } else if (annotation instanceof Delete) {
            return handleDelete((Delete) annotation);
        } else if (annotation instanceof Listing) {
            return handleListing((Listing) annotation);
        } else {
            throw new UnsupportedAnnotationException();
        }
    }

    default T handleRead(Read read) {
        throw new UnsupportedAnnotationException();
    }

    default T handleWrite(Write write) {
        throw new UnsupportedAnnotationException();
    }

    default T handleWriteBytes(WriteBytes writeBytes) {
        throw new UnsupportedAnnotationException();
    }

    default T handleWriteString(WriteString writeString) {
        throw new UnsupportedAnnotationException();
    }

    default T handleWriteBoolean(WriteBoolean writeBoolean) {
        throw new UnsupportedAnnotationException();
    }

    default T handleWriteInteger(WriteInteger writeInteger) {
        throw new UnsupportedAnnotationException();
    }

    default T handleDelete(Delete delete) {
        throw new UnsupportedAnnotationException();
    }

    default T handleListing(Listing listing) {
        throw new UnsupportedAnnotationException();
    }
}
