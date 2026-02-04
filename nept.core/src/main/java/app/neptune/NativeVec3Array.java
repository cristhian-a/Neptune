package app.neptune;

import app.neptune.dto.Vec3;
import app.neptune.struct.Vec3Layouts;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

final class NativeVec3Array implements AutoCloseable {
    private final Arena arena;
    private final MemorySegment segment;
    private final int count;

    NativeVec3Array(int count) {
        this.arena = Arena.ofConfined();
        this.segment = arena.allocate(Vec3Layouts.VEC3, count);
        this.count = count;
    }

    void set(int i, Vec3 v) {
        long o = i * Vec3Layouts.VEC3.byteSize();
        Vec3Layouts.X.set(segment, o, v.x());
        Vec3Layouts.Y.set(segment, o, v.y());
        Vec3Layouts.Z.set(segment, o, v.z());
    }

    MemorySegment segment() {
        return segment;
    }

    int size() {
        return count;
    }

    @Override
    public void close() {
        arena.close();
    }
}
