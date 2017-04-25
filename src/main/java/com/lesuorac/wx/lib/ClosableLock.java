package com.lesuorac.wx.lib;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClosableLock implements Lock, Closeable {

    private ReentrantLock lock;

    public ClosableLock() {
        this.lock = new ReentrantLock();
    }

    public <T extends Throwable> void performAction(ThrowingRunnable<T> consumer)
            throws InterruptedException, IOException, T {
        try (Closeable ‿ = this.open()) {
            consumer.run();
        }
    }

    @Override
    public void lock() {
        this.lock.lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        this.lock.lockInterruptibly();
    }

    @Override
    public boolean tryLock() {
        return this.lock.tryLock();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return this.lock.tryLock(time, unit);
    }

    @Override
    public void unlock() {
        this.lock.unlock();
    }

    @Override
    public Condition newCondition() {
        return this.lock.newCondition();
    }

    public Closeable open() throws InterruptedException {
        this.lockInterruptibly();
        return this;
    }

    @Override
    public void close() throws IOException {
        this.lock.unlock();
    }
}
