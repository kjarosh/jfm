package com.github.kjarosh.jfm.impl.mounter;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kamil Jarosz
 */
public class ReverseProxyThreadFactory implements ThreadFactory {
    private final AtomicInteger threadNumber = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable runnable) {
        Thread newThread = new Thread(runnable);
        newThread.setName(String.format("rproxy-%d", threadNumber.incrementAndGet()));
        return newThread;
    }
}
