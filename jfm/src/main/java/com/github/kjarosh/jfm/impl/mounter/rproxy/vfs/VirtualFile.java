package com.github.kjarosh.jfm.impl.mounter.rproxy.vfs;

import java.util.List;

/**
 * @author Kamil Jarosz
 */
public interface VirtualFile {
    byte[] read() throws VFSException;

    void write(byte[] data) throws VFSException;

    void delete() throws VFSException;

    List<String> list() throws VFSException;
}
