package com.github.kjarosh.jfm.tests.complex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Kamil Jarosz
 */
class PropertiesUtils {
    public static String propsToString(Properties props) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Writer w = new OutputStreamWriter(bos);
        try {
            props.store(w, null);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return new String(bos.toByteArray(), StandardCharsets.UTF_8);
    }

    public static String[] propsToStrings(Properties props) {
        List<String> ret = new ArrayList<>();
        props.forEach((key, value) -> ret.add(key.toString() + '=' + value + '\n'));
        return ret.toArray(new String[0]);
    }
}
