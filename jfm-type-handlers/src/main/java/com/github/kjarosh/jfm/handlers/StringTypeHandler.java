package com.github.kjarosh.jfm.handlers;

import java.nio.charset.StandardCharsets;

public class StringTypeHandler extends AbstractTypeHandler<String> {
    @Override
    public Class<String> getHandledClass() {
        return String.class;
    }

    @Override
    public String handleRead(byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }
}
