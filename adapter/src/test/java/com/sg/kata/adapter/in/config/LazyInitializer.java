package com.sg.kata.adapter.in.config;

public abstract class LazyInitializer<T> {

    private static final Object NO_INIT = new Object();

    @SuppressWarnings("unchecked")
    private volatile T object = (T) NO_INIT;

    public T get() {
        T result = object;

        if (result == NO_INIT) {
            synchronized (this) {
                result = object;
                if (result == NO_INIT) {
                    object = result = initialize();
                }
            }
        }

        return result;
    }

    protected abstract T initialize();
}
