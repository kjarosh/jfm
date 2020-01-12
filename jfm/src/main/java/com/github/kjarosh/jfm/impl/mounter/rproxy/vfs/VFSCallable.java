package com.github.kjarosh.jfm.impl.mounter.rproxy.vfs;

/**
 * @author Kamil Jarosz
 */
public interface VFSCallable<T> {
    T call() throws VFSException;
}
