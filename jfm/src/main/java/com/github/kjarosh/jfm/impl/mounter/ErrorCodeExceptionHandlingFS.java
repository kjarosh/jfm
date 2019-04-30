package com.github.kjarosh.jfm.impl.mounter;

import jnr.ffi.Pointer;
import ru.serce.jnrfuse.AbstractFuseFS;
import ru.serce.jnrfuse.FuseFS;
import ru.serce.jnrfuse.FuseFillDir;
import ru.serce.jnrfuse.struct.FileStat;
import ru.serce.jnrfuse.struct.Flock;
import ru.serce.jnrfuse.struct.FuseBufvec;
import ru.serce.jnrfuse.struct.FuseFileInfo;
import ru.serce.jnrfuse.struct.FusePollhandle;
import ru.serce.jnrfuse.struct.Statvfs;
import ru.serce.jnrfuse.struct.Timespec;

import java.util.function.Supplier;

/**
 * @author Kamil Jarosz
 */
class ErrorCodeExceptionHandlingFS extends AbstractFuseFS {
    private final FuseFS inner;

    ErrorCodeExceptionHandlingFS(FuseFS inner) {
        this.inner = inner;
    }

    private int handle(Supplier<Integer> code) {
        try {
            return code.get();
        } catch (ErrorCodeException e) {
            return e.getErrorCode();
        }
    }

    @Override
    public int getattr(String path, FileStat stat) {
        return handle(() -> inner.getattr(path, stat));
    }

    @Override
    public int readlink(String path, Pointer buf, long size) {
        return handle(() -> inner.readlink(path, buf, size));
    }

    @Override
    public int mknod(String path, long mode, long rdev) {
        return handle(() -> inner.mknod(path, mode, rdev));
    }

    @Override
    public int mkdir(String path, long mode) {
        return handle(() -> inner.mkdir(path, mode));
    }

    @Override
    public int unlink(String path) {
        return handle(() -> inner.unlink(path));
    }

    @Override
    public int rmdir(String path) {
        return handle(() -> inner.rmdir(path));
    }

    @Override
    public int symlink(String oldpath, String newpath) {
        return handle(() -> inner.symlink(oldpath, newpath));
    }

    @Override
    public int rename(String oldpath, String newpath) {
        return handle(() -> inner.rename(oldpath, newpath));
    }

    @Override
    public int link(String oldpath, String newpath) {
        return handle(() -> inner.link(oldpath, newpath));
    }

    @Override
    public int chmod(String path, long mode) {
        return handle(() -> inner.chmod(path, mode));
    }

    @Override
    public int chown(String path, long uid, long gid) {
        return handle(() -> inner.chown(path, uid, gid));
    }

    @Override
    public int truncate(String path, long size) {
        return handle(() -> inner.truncate(path, size));
    }

    @Override
    public int open(String path, FuseFileInfo fi) {
        return handle(() -> inner.open(path, fi));
    }

    @Override
    public int read(String path, Pointer buf, long size, long offset, FuseFileInfo fi) {
        return handle(() -> inner.read(path, buf, size, offset, fi));
    }

    @Override
    public int write(String path, Pointer buf, long size, long offset, FuseFileInfo fi) {
        return handle(() -> inner.write(path, buf, size, offset, fi));
    }

    @Override
    public int statfs(String path, Statvfs stbuf) {
        return handle(() -> inner.statfs(path, stbuf));
    }

    @Override
    public int flush(String path, FuseFileInfo fi) {
        return handle(() -> inner.flush(path, fi));
    }

    @Override
    public int release(String path, FuseFileInfo fi) {
        return handle(() -> inner.release(path, fi));
    }

    @Override
    public int fsync(String path, int isdatasync, FuseFileInfo fi) {
        return handle(() -> inner.fsync(path, isdatasync, fi));
    }

    @Override
    public int setxattr(String path, String name, Pointer value, long size, int flags) {
        return handle(() -> inner.setxattr(path, name, value, size, flags));
    }

    @Override
    public int getxattr(String path, String name, Pointer value, long size) {
        return handle(() -> inner.getxattr(path, name, value, size));
    }

    @Override
    public int listxattr(String path, Pointer list, long size) {
        return handle(() -> inner.listxattr(path, list, size));
    }

    @Override
    public int removexattr(String path, String name) {
        return handle(() -> inner.removexattr(path, name));
    }

    @Override
    public int opendir(String path, FuseFileInfo fi) {
        return handle(() -> inner.opendir(path, fi));
    }

    @Override
    public int readdir(String path, Pointer buf, FuseFillDir filter, long offset, FuseFileInfo fi) {
        return handle(() -> inner.readdir(path, buf, filter, offset, fi));
    }

    @Override
    public int releasedir(String path, FuseFileInfo fi) {
        return handle(() -> inner.releasedir(path, fi));
    }

    @Override
    public int fsyncdir(String path, FuseFileInfo fi) {
        return handle(() -> inner.fsyncdir(path, fi));
    }

    @Override
    public Pointer init(Pointer conn) {
        return inner.init(conn);
    }

    @Override
    public void destroy(Pointer initResult) {
        inner.destroy(initResult);
    }

    @Override
    public int access(String path, int mask) {
        return handle(() -> inner.access(path, mask));
    }

    @Override
    public int create(String path, long mode, FuseFileInfo fi) {
        return handle(() -> inner.create(path, mode, fi));
    }

    @Override
    public int ftruncate(String path, long size, FuseFileInfo fi) {
        return handle(() -> inner.ftruncate(path, size, fi));
    }

    @Override
    public int fgetattr(String path, FileStat stbuf, FuseFileInfo fi) {
        return handle(() -> inner.fgetattr(path, stbuf, fi));
    }

    @Override
    public int lock(String path, FuseFileInfo fi, int cmd, Flock flock) {
        return handle(() -> inner.lock(path, fi, cmd, flock));
    }

    @Override
    public int utimens(String path, Timespec[] timespec) {
        return handle(() -> inner.utimens(path, timespec));
    }

    @Override
    public int bmap(String path, long blocksize, long idx) {
        return handle(() -> inner.bmap(path, blocksize, idx));
    }

    @Override
    public int ioctl(String path, int cmd, Pointer arg, FuseFileInfo fi, long flags, Pointer data) {
        return handle(() -> inner.ioctl(path, cmd, arg, fi, flags, data));
    }

    @Override
    public int poll(String path, FuseFileInfo fi, FusePollhandle ph, Pointer reventsp) {
        return handle(() -> inner.poll(path, fi, ph, reventsp));
    }

    @Override
    public int write_buf(String path, FuseBufvec buf, long off, FuseFileInfo fi) {
        return handle(() -> inner.write_buf(path, buf, off, fi));
    }

    @Override
    public int read_buf(String path, Pointer bufp, long size, long off, FuseFileInfo fi) {
        return handle(() -> inner.read_buf(path, bufp, size, off, fi));
    }

    @Override
    public int flock(String path, FuseFileInfo fi, int op) {
        return handle(() -> inner.flock(path, fi, op));
    }

    @Override
    public int fallocate(String path, int mode, long off, long length, FuseFileInfo fi) {
        return handle(() -> inner.fallocate(path, mode, off, length, fi));
    }
}
