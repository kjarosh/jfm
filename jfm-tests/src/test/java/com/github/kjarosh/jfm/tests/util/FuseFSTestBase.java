package com.github.kjarosh.jfm.tests.util;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import ru.serce.jnrfuse.FuseFS;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Kamil Jarosz
 */
public abstract class FuseFSTestBase {
    protected abstract Stream<DynamicTest> testMockedMethod(Function<FuseFS, Integer> method);

    private <T> T objArg() {
        return null;
    }

    private long longArg() {
        return 0;
    }

    private int intArg() {
        return 0;
    }

    @TestFactory
    Stream<DynamicTest> getattr() {
        return testMockedMethod(fs -> fs.getattr(objArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> readlink() {
        return testMockedMethod(fs -> fs.readlink(objArg(), objArg(), longArg()));
    }

    @TestFactory
    Stream<DynamicTest> mknod() {
        return testMockedMethod(fs -> fs.mknod(objArg(), longArg(), longArg()));
    }

    @TestFactory
    Stream<DynamicTest> mkdir() {
        return testMockedMethod(fs -> fs.mkdir(objArg(), longArg()));
    }

    @TestFactory
    Stream<DynamicTest> unlink() {
        return testMockedMethod(fs -> fs.unlink(objArg()));
    }

    @TestFactory
    Stream<DynamicTest> rmdir() {
        return testMockedMethod(fs -> fs.rmdir(objArg()));
    }

    @TestFactory
    Stream<DynamicTest> symlink() {
        return testMockedMethod(fs -> fs.symlink(objArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> rename() {
        return testMockedMethod(fs -> fs.rename(objArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> link() {
        return testMockedMethod(fs -> fs.link(objArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> chmod() {
        return testMockedMethod(fs -> fs.chmod(objArg(), longArg()));
    }

    @TestFactory
    Stream<DynamicTest> chown() {
        return testMockedMethod(fs -> fs.chown(objArg(), longArg(), longArg()));
    }

    @TestFactory
    Stream<DynamicTest> truncate() {
        return testMockedMethod(fs -> fs.truncate(objArg(), longArg()));
    }

    @TestFactory
    Stream<DynamicTest> open() {
        return testMockedMethod(fs -> fs.open(objArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> read() {
        return testMockedMethod(fs -> fs.read(objArg(), objArg(), longArg(), longArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> write() {
        return testMockedMethod(fs -> fs.write(objArg(), objArg(), longArg(), longArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> statfs() {
        return testMockedMethod(fs -> fs.statfs(objArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> flush() {
        return testMockedMethod(fs -> fs.flush(objArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> release() {
        return testMockedMethod(fs -> fs.release(objArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> fsync() {
        return testMockedMethod(fs -> fs.fsync(objArg(), intArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> setxattr() {
        return testMockedMethod(fs -> fs.setxattr(objArg(), objArg(), objArg(), longArg(), intArg()));
    }

    @TestFactory
    Stream<DynamicTest> getxattr() {
        return testMockedMethod(fs -> fs.getxattr(objArg(), objArg(), objArg(), longArg()));
    }

    @TestFactory
    Stream<DynamicTest> listxattr() {
        return testMockedMethod(fs -> fs.listxattr(objArg(), objArg(), longArg()));
    }

    @TestFactory
    Stream<DynamicTest> removexattr() {
        return testMockedMethod(fs -> fs.removexattr(objArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> opendir() {
        return testMockedMethod(fs -> fs.opendir(objArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> readdir() {
        return testMockedMethod(fs -> fs.readdir(objArg(), objArg(), objArg(), longArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> releasedir() {
        return testMockedMethod(fs -> fs.releasedir(objArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> fsyncdir() {
        return testMockedMethod(fs -> fs.fsyncdir(objArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> access() {
        return testMockedMethod(fs -> fs.access(objArg(), intArg()));
    }

    @TestFactory
    Stream<DynamicTest> create() {
        return testMockedMethod(fs -> fs.create(objArg(), longArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> ftruncate() {
        return testMockedMethod(fs -> fs.ftruncate(objArg(), longArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> fgetattr() {
        return testMockedMethod(fs -> fs.fgetattr(objArg(), objArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> lock() {
        return testMockedMethod(fs -> fs.lock(objArg(), objArg(), intArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> utimens() {
        return testMockedMethod(fs -> fs.utimens(objArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> bmap() {
        return testMockedMethod(fs -> fs.bmap(objArg(), longArg(), longArg()));
    }

    @TestFactory
    Stream<DynamicTest> ioctl() {
        return testMockedMethod(fs -> fs.ioctl(objArg(), intArg(), objArg(), objArg(), longArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> poll() {
        return testMockedMethod(fs -> fs.poll(objArg(), objArg(), objArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> write_buf() {
        return testMockedMethod(fs -> fs.write_buf(objArg(), objArg(), longArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> read_buf() {
        return testMockedMethod(fs -> fs.read_buf(objArg(), objArg(), longArg(), longArg(), objArg()));
    }

    @TestFactory
    Stream<DynamicTest> flock() {
        return testMockedMethod(fs -> fs.flock(objArg(), objArg(), intArg()));
    }

    @TestFactory
    Stream<DynamicTest> fallocate() {
        return testMockedMethod(fs -> fs.fallocate(objArg(), intArg(), longArg(), longArg(), objArg()));
    }
}
