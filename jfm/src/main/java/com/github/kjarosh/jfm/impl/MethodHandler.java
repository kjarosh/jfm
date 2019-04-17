package com.github.kjarosh.jfm.impl;

import com.github.kjarosh.jfm.api.annotations.Delete;
import com.github.kjarosh.jfm.api.annotations.Listing;
import com.github.kjarosh.jfm.api.annotations.Read;
import com.github.kjarosh.jfm.api.annotations.Write;
import com.github.kjarosh.jfm.api.annotations.WriteBoolean;
import com.github.kjarosh.jfm.api.annotations.WriteBytes;
import com.github.kjarosh.jfm.api.annotations.WriteInteger;
import com.github.kjarosh.jfm.api.annotations.WriteString;

/**
 * @author Kamil Jarosz
 */
public interface MethodHandler<T> {
    default T handleRead(Read read) {
        throw new UnsupportedMethodException();
    }

    default T handleWrite(Write write) {
        throw new UnsupportedMethodException();
    }

    default T handleWriteBytes(WriteBytes writeBytes) {
        throw new UnsupportedMethodException();
    }

    default T handleWriteString(WriteString writeString) {
        throw new UnsupportedMethodException();
    }

    default T handleWriteBoolean(WriteBoolean writeBoolean) {
        throw new UnsupportedMethodException();
    }

    default T handleWriteInteger(WriteInteger writeInteger) {
        throw new UnsupportedMethodException();
    }

    default T handleDelete(Delete delete) {
        throw new UnsupportedMethodException();
    }

    default T handleListing(Listing listing) {
        throw new UnsupportedMethodException();
    }
}
