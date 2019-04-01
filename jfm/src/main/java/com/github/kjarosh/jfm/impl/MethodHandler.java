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
    T handleRead(Read read);

    T handleWrite(Write write);

    T handleWriteBytes(WriteBytes writeBytes);

    T handleWriteString(WriteString writeString);

    T handleWriteBoolean(WriteBoolean writeBoolean);

    T handleWriteInteger(WriteInteger writeInteger);

    T handleDelete(Delete delete);

    T handleListing(Listing listing);
}
