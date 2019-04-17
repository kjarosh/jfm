package com.github.kjarosh.jfm.impl.mounter;

import com.github.kjarosh.jfm.impl.mounter.rproxy.NoHandlerMethodException;
import com.github.kjarosh.jfm.impl.mounter.rproxy.ReverseProxy;
import jnr.constants.platform.OpenFlags;
import jnr.ffi.Pointer;
import jnr.ffi.Struct;
import jnr.ffi.types.off_t;
import jnr.ffi.types.size_t;
import ru.serce.jnrfuse.ErrorCodes;
import ru.serce.jnrfuse.FuseFillDir;
import ru.serce.jnrfuse.FuseStubFS;
import ru.serce.jnrfuse.struct.FileStat;
import ru.serce.jnrfuse.struct.FuseFileInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Kamil Jarosz
 */
public class FilesystemMapperFS extends FuseStubFS {
    private ReverseProxy reverseProxy;

    private Map<String, byte[]> openFiles = new HashMap<>();
    private Map<String, EnumSet<OpenFlags>> openFileFlags = new HashMap<>();

    FilesystemMapperFS(ReverseProxy reverseProxy) {
        this.reverseProxy = reverseProxy;
    }

    private boolean isNotOpen(String path) {
        return !openFiles.containsKey(path);
    }

    private boolean flushNeeded(String path) {
        return !Collections.disjoint(openFileFlags.get(path),
                Arrays.asList(OpenFlags.O_RDWR, OpenFlags.O_WRONLY));
    }

    private void handleErrorCodes(Runnable code) throws ErrorCodeException {
        try {
            code.run();
        } catch (NoHandlerMethodException e) {
            throw new ErrorCodeException(-ErrorCodes.ENOENT());
        }
    }

    private EnumSet<OpenFlags> mapOpenFlags(Struct.Signed32 flags) {
        Collection<OpenFlags> mappedFlags = new ArrayList<>();
        for (OpenFlags of : OpenFlags.values()) {
            if ((flags.get() & of.intValue()) != 0) {
                mappedFlags.add(of);
            }
        }

        return EnumSet.copyOf(mappedFlags);
    }

    @Override
    public int getattr(String path, FileStat stat) {
        int res = 0;
        if (Objects.equals(path, "/") || reverseProxy.isDirectory(path)) {
            stat.st_mode.set(FileStat.S_IFDIR | 0755);
            stat.st_nlink.set(1 + reverseProxy.list(path).size());
        } else if (reverseProxy.exists(path)) {
            stat.st_mode.set(FileStat.S_IFREG | 0444);
            stat.st_nlink.set(1);
        } else {
            res = -ErrorCodes.ENOENT();
        }
        return res;
    }

    @Override
    public int readdir(String path, Pointer buf, FuseFillDir filter, @off_t long offset, FuseFileInfo fi) {
        if (!reverseProxy.isDirectory(path)) {
            return -ErrorCodes.ENOENT();
        }

        filter.apply(buf, ".", null, 0);
        filter.apply(buf, "..", null, 0);
        reverseProxy.list(path)
                .forEach(file -> filter.apply(buf, file, null, 0));
        return 0;
    }

    @Override
    public int open(String path, FuseFileInfo fi) {
        if (!reverseProxy.exists(path)) {
            return -ErrorCodes.ENOENT();
        }

        if (reverseProxy.isDirectory(path)) {
            return -ErrorCodes.EISDIR();
        }

        try {
            handleErrorCodes(() -> openFiles.put(path, reverseProxy.readFile(path)));
            handleErrorCodes(() -> openFileFlags.put(path, mapOpenFlags(fi.flags)));
        } catch (ErrorCodeException e) {
            return e.getErrorCode();
        }

        return 0;
    }

    @Override
    public int flush(String path, FuseFileInfo fi) {
        if (isNotOpen(path)) {
            return -ErrorCodes.EBADFD();
        }

        if (!flushNeeded(path)) {
            return 0;
        }

        try {
            handleErrorCodes(() -> reverseProxy.writeFile(path, openFiles.get(path)));
        } catch (ErrorCodeException e) {
            return e.getErrorCode();
        }

        return 0;
    }

    @Override
    public int release(String path, FuseFileInfo fi) {
        if (isNotOpen(path)) {
            return -ErrorCodes.EBADFD();
        }

        openFiles.remove(path);
        return 0;
    }

    @Override
    public int read(String path, Pointer buf, @size_t long size, @off_t long offset, FuseFileInfo fi) {
        boolean opened = false;
        if (isNotOpen(path)) {
            opened = true;
            open(path, fi);
        }

        byte[] bytes = openFiles.get(path);
        int length = bytes.length;
        if (offset < length) {
            if (offset + size > length) {
                size = length - offset;
            }
            buf.put(0, bytes, (int) offset, (int) size);
        } else {
            size = 0;
        }

        if (opened) {
            release(path, fi);
        }
        return (int) size;
    }

    @Override
    public int write(String path, Pointer buf, long size, long offset, FuseFileInfo fi) {
        if (isNotOpen(path)) {
            return -ErrorCodes.EBADFD();
        }

        byte[] bytes = openFiles.get(path);
        if (bytes.length < offset + size) {
            bytes = Arrays.copyOf(bytes, (int) (offset + size));
            openFiles.put(path, bytes);
        }
        buf.get(0, bytes, (int) offset, (int) size);
        return (int) size;
    }

    @Override
    public int truncate(String path, long size) {
        if (isNotOpen(path)) {
            return -ErrorCodes.EBADFD();
        }

        byte[] oldBytes = openFiles.get(path);
        openFiles.put(path, Arrays.copyOf(oldBytes, (int) size));
        return 0;
    }
}
