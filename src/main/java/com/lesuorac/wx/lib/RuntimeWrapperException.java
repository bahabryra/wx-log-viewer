package com.lesuorac.wx.lib;

public class RuntimeWrapperException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Throwable underlyingException;

    public RuntimeWrapperException(Throwable underlyingException) {
        this.underlyingException = underlyingException;
    }

    public static final <O extends Object, T extends Throwable> O wrap(ThrowingSupplier<O, T> lambda)
            throws RuntimeWrapperException {
        try {
            return lambda.run();
        } catch (Throwable e) {
            throw new RuntimeWrapperException(e);
        }
    }

    public static final <T extends Throwable> void wrap(ThrowingRunnable<T> lambda) throws RuntimeWrapperException {
        try {
            lambda.run();
        } catch (Throwable e) {
            throw new RuntimeWrapperException(e);
        }
    }

    /**
     * @return the underlyingException
     * @see #bare_field_name
     */
    public final Throwable getUnderlyingException() {
        return this.underlyingException;
    }

}
