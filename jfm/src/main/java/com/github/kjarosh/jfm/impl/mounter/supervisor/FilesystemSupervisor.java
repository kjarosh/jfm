package com.github.kjarosh.jfm.impl.mounter.supervisor;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Kamil Jarosz
 */
public class FilesystemSupervisor extends Thread {
    private static final long TIMEOUT_SEC = 60;
    private static final long TIMEOUT2_SEC = 10;

    private final Thread supervised;
    private boolean supervisionEnabled = false;

    private Lock lock = new ReentrantLock();
    private Condition supervisionEnabledCondition = lock.newCondition();

    public FilesystemSupervisor(Thread supervised) {
        super.setName("FilesystemSupervisor");
        this.supervised = supervised;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                runInterruptible();
            }
        } catch (InterruptedException e) {
            return;
        }
    }

    private void runInterruptible() throws InterruptedException {
        lock.lock();
        try {
            while (!supervisionEnabled) {
                supervisionEnabledCondition.await();
            }

            // wait for the process to finish its work
            Date deadline = Date.from(Instant.now().plusSeconds(TIMEOUT_SEC));
            if (waitForGracefulShutdown(deadline)) {
                return;
            }

            supervised.interrupt();
            supervised.join(TIMEOUT2_SEC * 1000);

            if (!supervised.isInterrupted()) {
                // we have to force the thread to stop
                supervised.stop(new SupervisedDeath());
            }

            supervisionEnabled = false;
        } finally {
            lock.unlock();
        }
    }

    private boolean waitForGracefulShutdown(Date deadline) throws InterruptedException {
        boolean elapsed = false;
        while (supervisionEnabled && !elapsed) {
            elapsed = supervisionEnabledCondition.awaitUntil(deadline);
        }
        return !supervisionEnabled;
    }

    public SupervisingScope startSupervising() {
        lock.lock();
        try {
            supervisionEnabled = true;
            supervisionEnabledCondition.signalAll();
            return this::finishSupervising;
        } finally {
            lock.unlock();
        }
    }

    public void finishSupervising() {
        lock.lock();
        try {
            supervisionEnabled = false;
            supervisionEnabledCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
