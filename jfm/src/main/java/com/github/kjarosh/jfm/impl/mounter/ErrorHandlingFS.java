package com.github.kjarosh.jfm.impl.mounter;

import jnr.ffi.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.serce.jnrfuse.AbstractFuseFS;
import ru.serce.jnrfuse.ErrorCodes;
import ru.serce.jnrfuse.FuseFS;
import ru.serce.jnrfuse.FuseFillDir;
import ru.serce.jnrfuse.struct.FileStat;
import ru.serce.jnrfuse.struct.Flock;
import ru.serce.jnrfuse.struct.FuseBufvec;
import ru.serce.jnrfuse.struct.FuseFileInfo;
import ru.serce.jnrfuse.struct.FusePollhandle;
import ru.serce.jnrfuse.struct.Statvfs;
import ru.serce.jnrfuse.struct.Timespec;

/**
 * @author Kamil Jarosz
 */
public class ErrorHandlingFS extends AbstractFuseFS {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingFS.class);
    private static final int ERROR = -ErrorCodes.EBADFD();

    private final FuseFS inner;

    ErrorHandlingFS(FuseFS inner) {
        this.inner = inner;
    }

    private void log(Exception ex) {
        String func = new Throwable().getStackTrace()[1].getMethodName();
        logger.error("Error while invoking " + func, ex);
    }

    @Override
    public int getattr(String path, FileStat stat) {
        try {
            return inner.getattr(path, stat);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int readlink(String path, Pointer buf, long size) {
        try {
            return inner.readlink(path, buf, size);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int mknod(String path, long mode, long rdev) {
        try {
            return inner.mknod(path, mode, rdev);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int mkdir(String path, long mode) {
        try {
            return inner.mkdir(path, mode);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int unlink(String path) {
        try {
            return inner.unlink(path);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int rmdir(String path) {
        try {
            return inner.rmdir(path);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int symlink(String oldpath, String newpath) {
        try {
            return inner.symlink(oldpath, newpath);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int rename(String oldpath, String newpath) {
        try {
            return inner.rename(oldpath, newpath);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int link(String oldpath, String newpath) {
        try {
            return inner.link(oldpath, newpath);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int chmod(String path, long mode) {
        try {
            return inner.chmod(path, mode);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int chown(String path, long uid, long gid) {
        try {
            return inner.chown(path, uid, gid);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int truncate(String path, long size) {
        try {
            return inner.truncate(path, size);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int open(String path, FuseFileInfo fi) {
        try {
            return inner.open(path, fi);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int read(String path, Pointer buf, long size, long offset, FuseFileInfo fi) {
        try {
            return inner.read(path, buf, size, offset, fi);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int write(String path, Pointer buf, long size, long offset, FuseFileInfo fi) {
        try {
            return inner.write(path, buf, size, offset, fi);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int statfs(String path, Statvfs stbuf) {
        try {
            return inner.statfs(path, stbuf);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int flush(String path, FuseFileInfo fi) {
        try {
            return inner.flush(path, fi);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int release(String path, FuseFileInfo fi) {
        try {
            return inner.release(path, fi);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int fsync(String path, int isdatasync, FuseFileInfo fi) {
        try {
            return inner.fsync(path, isdatasync, fi);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int setxattr(String path, String name, Pointer value, long size, int flags) {
        try {
            return inner.setxattr(path, name, value, size, flags);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int getxattr(String path, String name, Pointer value, long size) {
        try {
            return inner.getxattr(path, name, value, size);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int listxattr(String path, Pointer list, long size) {
        try {
            return inner.listxattr(path, list, size);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int removexattr(String path, String name) {
        try {
            return inner.removexattr(path, name);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int opendir(String path, FuseFileInfo fi) {
        try {
            return inner.opendir(path, fi);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int readdir(String path, Pointer buf, FuseFillDir filter, long offset, FuseFileInfo fi) {
        try {
            return inner.readdir(path, buf, filter, offset, fi);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int releasedir(String path, FuseFileInfo fi) {
        try {
            return inner.releasedir(path, fi);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int fsyncdir(String path, FuseFileInfo fi) {
        try {
            return inner.fsyncdir(path, fi);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public Pointer init(Pointer conn) {
        try {
            return inner.init(conn);
        } catch (Exception e) {
            log(e);
            return null;
        }
    }

    @Override
    public void destroy(Pointer initResult) {
        try {
            inner.destroy(initResult);
        } catch (Exception e) {
            log(e);
        }
    }

    @Override
    public int access(String path, int mask) {
        try {
            return inner.access(path, mask);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int create(String path, long mode, FuseFileInfo fi) {
        try {
            return inner.create(path, mode, fi);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int ftruncate(String path, long size, FuseFileInfo fi) {
        try {
            return inner.ftruncate(path, size, fi);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int fgetattr(String path, FileStat stbuf, FuseFileInfo fi) {
        try {
            return inner.fgetattr(path, stbuf, fi);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int lock(String path, FuseFileInfo fi, int cmd, Flock flock) {
        try {
            return inner.lock(path, fi, cmd, flock);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int utimens(String path, Timespec[] timespec) {
        try {
            return inner.utimens(path, timespec);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int bmap(String path, long blocksize, long idx) {
        try {
            return inner.bmap(path, blocksize, idx);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int ioctl(String path, int cmd, Pointer arg, FuseFileInfo fi, long flags, Pointer data) {
        try {
            return inner.ioctl(path, cmd, arg, fi, flags, data);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int poll(String path, FuseFileInfo fi, FusePollhandle ph, Pointer reventsp) {
        try {
            return inner.poll(path, fi, ph, reventsp);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int write_buf(String path, FuseBufvec buf, long off, FuseFileInfo fi) {
        try {
            return inner.write_buf(path, buf, off, fi);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int read_buf(String path, Pointer bufp, long size, long off, FuseFileInfo fi) {
        try {
            return inner.read_buf(path, bufp, size, off, fi);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int flock(String path, FuseFileInfo fi, int op) {
        try {
            return inner.flock(path, fi, op);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }

    @Override
    public int fallocate(String path, int mode, long off, long length, FuseFileInfo fi) {
        try {
            return inner.fallocate(path, mode, off, length, fi);
        } catch (Exception e) {
            log(e);
            return ERROR;
        }
    }
}
