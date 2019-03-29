package com.github.kjarosh.jfm.impl.mounter;

import com.github.kjarosh.jfm.impl.mounter.rproxy.ReverseProxy;
import jnr.ffi.Pointer;
import jnr.ffi.types.off_t;
import jnr.ffi.types.size_t;
import ru.serce.jnrfuse.ErrorCodes;
import ru.serce.jnrfuse.FuseFillDir;
import ru.serce.jnrfuse.FuseStubFS;
import ru.serce.jnrfuse.struct.FileStat;
import ru.serce.jnrfuse.struct.FuseFileInfo;

/**
 * @author Kamil Jarosz
 */
public class FilesystemMapperFuseStub extends FuseStubFS {
    private ReverseProxy reverseProxy;

    FilesystemMapperFuseStub(ReverseProxy reverseProxy) {
        this.reverseProxy = reverseProxy;
    }

    @Override
    public int getattr(String path, FileStat stat) {
        int res = 0;
        /*if (Objects.equals(path, "/")) {
            stat.st_mode.set(FileStat.S_IFDIR | 0755);
            stat.st_nlink.set(2);
        } else if (HELLO_PATH.equals(path)) {
            stat.st_mode.set(FileStat.S_IFREG | 0444);
            stat.st_nlink.set(1);
            stat.st_size.set(HELLO_STR.getBytes().length);
        } else {
            res = -ErrorCodes.ENOENT();
        }*/
        return res;
    }

    @Override
    public int readdir(String path, Pointer buf, FuseFillDir filter, @off_t long offset, FuseFileInfo fi) {
        if (!reverseProxy.isDirectory(path)) {
            return -ErrorCodes.ENOENT();
        }

        filter.apply(buf, ".", null, 0);
        filter.apply(buf, "..", null, 0);
        reverseProxy.list(path).forEach(file -> filter.apply(buf, file, null, 0));
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

        return 0;
    }

    @Override
    public int read(String path, Pointer buf, @size_t long size, @off_t long offset, FuseFileInfo fi) {
        if (!reverseProxy.exists(path)) {
            return -ErrorCodes.ENOENT();
        }

        if (reverseProxy.isDirectory(path)) {
            return -ErrorCodes.EISDIR();
        }

        byte[] content = reverseProxy.readFile(path, size, offset);
        int length = content.length;
        if (length == 0) {
            return 0;
        }

        buf.put(0, content, 0, content.length);
        return length;
    }
}
