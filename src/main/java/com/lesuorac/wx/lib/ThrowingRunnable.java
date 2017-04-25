package com.lesuorac.wx.lib;

@FunctionalInterface
public interface ThrowingRunnable<T extends Throwable> {

    public void run() throws T;
}
