package com.github.kjarosh.jfm.impl.mounter.rproxy.vfs;

import java.util.List;

/**
 * @author Kamil Jarosz
 */
public interface VirtualFile {
    byte[] read();

    void write(byte[] data);

    void delete();

    List<String> list();
}
