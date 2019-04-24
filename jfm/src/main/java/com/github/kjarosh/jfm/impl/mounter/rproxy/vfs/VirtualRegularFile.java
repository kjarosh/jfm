package com.github.kjarosh.jfm.impl.mounter.rproxy.vfs;

import com.github.kjarosh.jfm.api.FilesystemMapperException;

import java.util.List;

/**
 * @author Kamil Jarosz
 */
public class VirtualRegularFile implements VirtualFile {
    private VirtualFileReadHandler readHandler;
    private VirtualFileWriteHandler writeHandler;
    private VirtualFileDeleteHandler deleteHandler;

    public void setReadHandler(VirtualFileReadHandler handler) {
        this.readHandler = handler;
    }

    public void setWriteHandler(VirtualFileWriteHandler handler) {
        this.writeHandler = handler;
    }

    public void setDeleteHandler(VirtualFileDeleteHandler handler) {
        this.deleteHandler = handler;
    }

    @Override
    public byte[] read() {
        return readHandler.read();
    }

    @Override
    public void write(byte[] data) {
        writeHandler.write(data);
    }

    @Override
    public void delete() {
        deleteHandler.delete();
    }

    @Override
    public List<String> list() {
        throw new FilesystemMapperException("Cannot list a regular file");
    }
}
