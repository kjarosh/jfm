package com.github.kjarosh.jfm.handlers;

public class ByteArrayTypeHandler extends AbstractTypeHandler<byte[]> {
    @Override
    public Class<byte[]> getHandledClass() {
        return byte[].class;
    }

    @Override
    public byte[] handleRead(byte[] data) {
        return data;
    }
}
