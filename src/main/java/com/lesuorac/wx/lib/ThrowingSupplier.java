package com.lesuorac.wx.lib;

public interface ThrowingSupplier<O extends Object, T extends Throwable> {
    public O run() throws T;
}
