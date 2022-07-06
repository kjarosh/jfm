package com.github.kjarosh.jfm.impl.mounter;

import com.github.kjarosh.jfm.impl.mounter.rproxy.NoHandlerMethodException;
import com.github.kjarosh.jfm.impl.mounter.rproxy.ReverseProxy;
import com.github.kjarosh.jfm.impl.mounter.rproxy.vfs.DirectoryRemovalException;
import com.github.kjarosh.jfm.impl.mounter.rproxy.vfs.IsDirectoryException;
import com.github.kjarosh.jfm.impl.mounter.rproxy.vfs.VFSCallable;
import com.github.kjarosh.jfm.impl.mounter.rproxy.vfs.VFSException;
import com.github.kjarosh.jfm.impl.mounter.rproxy.vfs.VFSRunnable;
import com.github.kjarosh.jfm.impl.util.Lazy;
import jnr.constants.platform.OpenFlags;
import jnr.ffi.Pointer;
import jnr.ffi.Struct;
import jnr.ffi.types.off_t;
import jnr.ffi.types.size_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(FilesystemMapperFS.class);

    private final ReverseProxy reverseProxy;

    private final Map<String, Lazy<byte[], VFSException>> openFiles = new HashMap<>();
    private final Map<String, EnumSet<OpenFlags>> openFileFlags = new HashMap<>();

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

    private <T> T handleExceptions(VFSCallable<T> code) {
        try {
            return code.call();
        } catch (NoHandlerMethodException e) {
            // TODO
            throw new ErrorCodeException(-ErrorCodes.ENOENT());
        } catch (IsDirectoryException e) {
            throw new ErrorCodeException(-ErrorCodes.EISDIR());
        } catch (DirectoryRemovalException e) {
            throw new ErrorCodeException(-ErrorCodes.EPERM());
        } catch (VFSException e) {
            logger.error("Unknown VFS error", e);
            throw new ErrorCodeException(-ErrorCodes.EBADFD());
        }
    }

    private void handleExceptions(VFSRunnable code) {
        handleExceptions((VFSCallable<Void>) () -> {
            code.run();
            return null;
        });
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
            handleExceptions(() -> stat.st_nlink.set(1 + reverseProxy.list(path).size()));
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

        handleExceptions(() -> reverseProxy.list(path)
                .forEach(file -> filter.apply(buf, file, null, 0)));

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

        handleExceptions(() -> openFiles.put(path, Lazy.of(() -> reverseProxy.readFile(path))));
        handleExceptions(() -> openFileFlags.put(path, mapOpenFlags(fi.flags)));

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

        handleExceptions(() -> reverseProxy.writeFile(path, openFiles.get(path).get()));

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
            int r = open(path, fi);
            if (r != 0) return r;
        }

        byte[] bytes = handleExceptions(() -> openFiles.get(path).get());
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

        byte[] bytes = handleExceptions(() -> openFiles.get(path).get());
        if (bytes.length < offset + size) {
            bytes = Arrays.copyOf(bytes, (int) (offset + size));
            openFiles.put(path, Lazy.ofEager(bytes));
        }
        buf.get(0, bytes, (int) offset, (int) size);
        return (int) size;
    }

    @Override
    public int truncate(String path, long size) {
        if (isNotOpen(path)) {
            return -ErrorCodes.EBADFD();
        }

        if (size == 0) {
            openFiles.put(path, Lazy.ofEager(new byte[0]));
        } else {
            byte[] oldBytes = handleExceptions(() -> openFiles.get(path).get());
            openFiles.put(path, Lazy.ofEager(Arrays.copyOf(oldBytes, (int) size)));
        }

        return 0;
    }

    @Override
    public int link(String oldpath, String newpath) {
        // links not supported
        return -ErrorCodes.EPERM();
    }

    @Override
    public int unlink(String path) {
        handleExceptions(() -> reverseProxy.deleteFile(path));

        return 0;
    }

    @Override
    public int create(String path, long mode, FuseFileInfo fi) {
        return -ErrorCodes.ENOSYS();
    }
}
