package app.neptune.example.opaque;

import java.lang.foreign.MemorySegment;

public final class NativeCounter implements AutoCloseable {
    private final MemorySegment handle;
    private boolean closed;

    private NativeCounter(MemorySegment handle) {
        this.handle = handle;
    }

    public static NativeCounter create(int initialValue) {
        MemorySegment h = NativeCounterNative.create(initialValue);
        if (h == MemorySegment.NULL) throw new OutOfMemoryError();
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
    public void close() {
        if (!closed) {
            NativeCounterNative.destroy(handle);
            closed = true;
        }
    }

    private void ensureOpen() {
        if (closed) throw new IllegalStateException("Counter is closed");
    }
}
