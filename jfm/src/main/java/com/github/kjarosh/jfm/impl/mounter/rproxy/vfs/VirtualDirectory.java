package com.github.kjarosh.jfm.impl.mounter.rproxy.vfs;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.impl.util.PathUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Kamil Jarosz
 */
public class VirtualDirectory implements VirtualFile {
    private final Map<String, VirtualFile> children = new HashMap<>();
    private VirtualFileListingHandler listingHandler;

    public Optional<VirtualDirectory> ensureDirectoryCreated(String path) {
        String name = PathUtils.getFirstName(path);
        Optional<String> rest = PathUtils.shift(path);
        if (rest.isPresent()) {
            return descend(name)
                    .filter(f -> f instanceof VirtualDirectory)
                    .map(f -> (VirtualDirectory) f)
                    .flatMap(f -> f.ensureDirectoryCreated(rest.get()));
        } else {
            VirtualFile file = children.computeIfAbsent(name, i -> new VirtualDirectory());
            if (file instanceof VirtualDirectory) {
                return Optional.of((VirtualDirectory) file);
            } else {
                return Optional.empty();
            }
        }
    }

    public Optional<VirtualFile> descend(String name) {
        return Optional.ofNullable(children.get(name));
    }

    public Optional<VirtualFile> resolve(String path) {
        String name = PathUtils.getFirstName(path);
        Optional<String> rest = PathUtils.shift(path);
        if (!rest.isPresent()) {
            return descend(name);
        }

        Optional<VirtualFile> child = descend(name);
        if (child.isPresent()) {
            if (!(child.get() instanceof VirtualDirectory)) {
                // trying to get a child not from a tree
                return Optional.empty();
            }

            return ((VirtualDirectory) child.get()).resolve(rest.get());
        } else {
            return Optional.empty();
        }
    }

    public void addChild(String name, VirtualFile file) {
        children.put(name, file);
    }

    public void setListingHandler(VirtualFileListingHandler handler) {
        this.listingHandler = handler;
    }

    @Override
    public byte[] read() {
        throw new FilesystemMapperException("Cannot read a directory");
    }

    @Override
    public void write(byte[] data) {
        throw new FilesystemMapperException("Cannot write a directory");
    }

    @Override
    public void delete() {
        throw new FilesystemMapperException("Cannot delete a directory");
    }

    @Override
    public List<String> list() {
        if (listingHandler != null) {
            return listingHandler.list();
        }

        return new ArrayList<>(children.keySet());
    }
}
