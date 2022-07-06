package com.github.kjarosh.jfm.impl.mounter;

import jnr.ffi.Pointer;
import lombok.extern.slf4j.Slf4j;
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

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Kamil Jarosz
 */
@Slf4j
class ThreadDelegatingFS extends AbstractFuseFS {
    private static final int ERROR = -ErrorCodes.EBADFD();

    private final ExecutorService executorService;
    private final FuseFS inner;
    private final Duration timeout;

    ThreadDelegatingFS(ExecutorService executorService, FuseFS inner, Duration timeout) {
        this.executorService = executorService;
        this.inner = inner;
        this.timeout = timeout;
    }

    private void delegate(Runnable task) {
        delegate((Callable<Void>) () -> {
            task.run();
            return null;
        });
    }

    private <T> Optional<T> delegate(Callable<T> task) {
        Future<T> future = executorService.submit(task);
        try {
            return Optional.ofNullable(future.get(timeout.toMillis(), TimeUnit.MILLISECONDS));
        } catch (InterruptedException e) {
            log.error("FUSE thread has been interrupted", e);
            // we cannot interrupt FUSE thread, we have to ignore this
        } catch (ExecutionException e) {
            log.error("The delegate method threw an exception", e);
        } catch (TimeoutException e) {
            log.error("Timed out waiting for the delegate method to complete", e);
        } finally {
            future.cancel(true);
        }

        return Optional.empty();
    }

    @Override
    public int getattr(String path, FileStat stat) {
        return delegate(() -> inner.getattr(path, stat)).orElse(ERROR);
    }

    @Override
    public int readlink(String path, Pointer buf, long size) {
        return delegate(() -> inner.readlink(path, buf, size)).orElse(ERROR);
    }

    @Override
    public int mknod(String path, long mode, long rdev) {
        return delegate(() -> inner.mknod(path, mode, rdev)).orElse(ERROR);
    }

    @Override
    public int mkdir(String path, long mode) {
        return delegate(() -> inner.mkdir(path, mode)).orElse(ERROR);
    }

    @Override
    public int unlink(String path) {
        return delegate(() -> inner.unlink(path)).orElse(ERROR);
    }

    @Override
    public int rmdir(String path) {
        return delegate(() -> inner.rmdir(path)).orElse(ERROR);
    }

    @Override
    public int symlink(String oldpath, String newpath) {
        return delegate(() -> inner.symlink(oldpath, newpath)).orElse(ERROR);
    }

    @Override
    public int rename(String oldpath, String newpath) {
        return delegate(() -> inner.rename(oldpath, newpath)).orElse(ERROR);
    }

    @Override
    public int link(String oldpath, String newpath) {
        return delegate(() -> inner.link(oldpath, newpath)).orElse(ERROR);
    }

    @Override
    public int chmod(String path, long mode) {
        return delegate(() -> inner.chmod(path, mode)).orElse(ERROR);
    }

    @Override
    public int chown(String path, long uid, long gid) {
        return delegate(() -> inner.chown(path, uid, gid)).orElse(ERROR);
    }

    @Override
    public int truncate(String path, long size) {
        return delegate(() -> inner.truncate(path, size)).orElse(ERROR);
    }

    @Override
    public int open(String path, FuseFileInfo fi) {
        return delegate(() -> inner.open(path, fi)).orElse(ERROR);
    }

    @Override
    public int read(String path, Pointer buf, long size, long offset, FuseFileInfo fi) {
        return delegate(() -> inner.read(path, buf, size, offset, fi)).orElse(ERROR);
    }

    @Override
    public int write(String path, Pointer buf, long size, long offset, FuseFileInfo fi) {
        return delegate(() -> inner.write(path, buf, size, offset, fi)).orElse(ERROR);
    }

    @Override
    public int statfs(String path, Statvfs stbuf) {
        return delegate(() -> inner.statfs(path, stbuf)).orElse(ERROR);
    }

    @Override
    public int flush(String path, FuseFileInfo fi) {
        return delegate(() -> inner.flush(path, fi)).orElse(ERROR);
    }

    @Override
    public int release(String path, FuseFileInfo fi) {
        return delegate(() -> inner.release(path, fi)).orElse(ERROR);
    }

    @Override
    public int fsync(String path, int isdatasync, FuseFileInfo fi) {
        return delegate(() -> inner.fsync(path, isdatasync, fi)).orElse(ERROR);
    }

    @Override
    public int setxattr(String path, String name, Pointer value, long size, int flags) {
        return delegate(() -> inner.setxattr(path, name, value, size, flags)).orElse(ERROR);
    }

    @Override
    public int getxattr(String path, String name, Pointer value, long size) {
        return delegate(() -> inner.getxattr(path, name, value, size)).orElse(ERROR);
    }

    @Override
    public int listxattr(String path, Pointer list, long size) {
        return delegate(() -> inner.listxattr(path, list, size)).orElse(ERROR);
    }

    @Override
    public int removexattr(String path, String name) {
        return delegate(() -> inner.removexattr(path, name)).orElse(ERROR);
    }

    @Override
    public int opendir(String path, FuseFileInfo fi) {
        return delegate(() -> inner.opendir(path, fi)).orElse(ERROR);
    }

    @Override
    public int readdir(String path, Pointer buf, FuseFillDir filter, long offset, FuseFileInfo fi) {
        return delegate(() -> inner.readdir(path, buf, filter, offset, fi)).orElse(ERROR);
    }

    @Override
    public int releasedir(String path, FuseFileInfo fi) {
        return delegate(() -> inner.releasedir(path, fi)).orElse(ERROR);
    }

    @Override
    public int fsyncdir(String path, FuseFileInfo fi) {
        return delegate(() -> inner.fsyncdir(path, fi)).orElse(ERROR);
    }

    @Override
    public Pointer init(Pointer conn) {
        return delegate(() -> inner.init(conn)).orElse(null);
    }

    @Override
    public void destroy(Pointer initResult) {
        delegate(() -> inner.destroy(initResult));
    }

    @Override
    public int access(String path, int mask) {
        return delegate(() -> inner.access(path, mask)).orElse(ERROR);
    }

    @Override
    public int create(String path, long mode, FuseFileInfo fi) {
        return delegate(() -> inner.create(path, mode, fi)).orElse(ERROR);
    }

    @Override
    public int ftruncate(String path, long size, FuseFileInfo fi) {
        return delegate(() -> inner.ftruncate(path, size, fi)).orElse(ERROR);
    }

    @Override
    public int fgetattr(String path, FileStat stbuf, FuseFileInfo fi) {
        return delegate(() -> inner.fgetattr(path, stbuf, fi)).orElse(ERROR);
    }

    @Override
    public int lock(String path, FuseFileInfo fi, int cmd, Flock flock) {
        return delegate(() -> inner.lock(path, fi, cmd, flock)).orElse(ERROR);
    }

    @Override
    public int utimens(String path, Timespec[] timespec) {
        return delegate(() -> inner.utimens(path, timespec)).orElse(ERROR);
    }

    @Override
    public int bmap(String path, long blocksize, long idx) {
        return delegate(() -> inner.bmap(path, blocksize, idx)).orElse(ERROR);
    }

    @Override
    public int ioctl(String path, int cmd, Pointer arg, FuseFileInfo fi, long flags, Pointer data) {
        return delegate(() -> inner.ioctl(path, cmd, arg, fi, flags, data)).orElse(ERROR);
    }

    @Override
    public int poll(String path, FuseFileInfo fi, FusePollhandle ph, Pointer reventsp) {
        return delegate(() -> inner.poll(path, fi, ph, reventsp)).orElse(ERROR);
    }

    @Override
    public int write_buf(String path, FuseBufvec buf, long off, FuseFileInfo fi) {
        return delegate(() -> inner.write_buf(path, buf, off, fi)).orElse(ERROR);
    }

    @Override
    public int read_buf(String path, Pointer bufp, long size, long off, FuseFileInfo fi) {
        return delegate(() -> inner.read_buf(path, bufp, size, off, fi)).orElse(ERROR);
    }

    @Override
    public int flock(String path, FuseFileInfo fi, int op) {
        return delegate(() -> inner.flock(path, fi, op)).orElse(ERROR);
    }

    @Override
    public int fallocate(String path, int mode, long off, long length, FuseFileInfo fi) {
        return delegate(() -> inner.fallocate(path, mode, off, length, fi)).orElse(ERROR);
    }
}
