package app.neptune.example.opaque;

public final class NativeCounter implements AutoCloseable {
    private final long handle;
    private boolean closed;

    private NativeCounter(long handle) {
        this.handle = handle;
    }

    public static NativeCounter create(int initialValue) {
        long h = NativeCounterNative.create(initialValue);
        if (h == 0) throw new OutOfMemoryError();
        return new NativeCounter(h);
    }

    public int get() {
        ensureOpen();
        return NativeCounterNative.get(handle);
    }

    public void increment() {
        ensureOpen();
        NativeCounterNative.increment(handle);
    }

    @Override
    public void close() throws Exception {
        if (!closed) {
            NativeCounterNative.destroy(handle);
            closed = true;
        }
    }

    private void ensureOpen() {
        if (closed) throw new IllegalStateException("Counter is closed");
    }
}
