package app.neptune;

import app.neptune.dto.Vec3;
import app.neptune.struct.Vec3Layouts;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;

public final class NeptVec {

    public static void call() {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment vec = arena.allocate(Vec3Layouts.VEC3);
            Vec3Layouts.X.set(vec, (long) 0, 3f);
            Vec3Layouts.Y.set(vec, (long) 0, 4f);
            Vec3Layouts.Z.set(vec, (long) 0, 0f);

            float len = (float) Vec3Layouts.VEC3_LENGTH.invokeExact(vec);
            IO.println("vec3_length");
            IO.println(len);

            Vec3Layouts.VEC3_NORMALIZE.invokeExact(vec);
            IO.println("vec3_normalize");
            IO.println(
                    (float) Vec3Layouts.X.get(vec, (long) 0) + ", " +
                    (float) Vec3Layouts.Y.get(vec, (long) 0) + ", " +
                    (float) Vec3Layouts.Z.get(vec, (long) 0)
            );

            MemorySegment vec2 = arena.allocate(Vec3Layouts.VEC3);
            Vec3Layouts.X.set(vec2, (long) 0, 2f);
            Vec3Layouts.Y.set(vec2, (long) 0, 3f);
            Vec3Layouts.Z.set(vec2, (long) 0, 1f);

            var vecOut = (MemorySegment) Vec3Layouts.VEC3_ADD
                    .invokeExact((SegmentAllocator) arena, vec, vec2);
            IO.println("vec3_add");
            IO.println(
                    (float) Vec3Layouts.X.get(vecOut, (long) 0) + ", " +
                    (float) Vec3Layouts.Y.get(vecOut, (long) 0) + ", " +
                    (float) Vec3Layouts.Z.get(vecOut, (long) 0)
            );
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        IO.println();
    }

    public static void addAll(List<Vec3> a, List<Vec3> b) {
        if (a.size() != b.size()) return;
        if (a.isEmpty()) return;

        try (Arena arena = Arena.ofConfined()) {
            long count = a.size();

            MemorySegment segA = arena.allocate(Vec3Layouts.VEC3, count);
            MemorySegment segB = arena.allocate(Vec3Layouts.VEC3, count);
            MemorySegment segR = arena.allocate(Vec3Layouts.VEC3, count);

            long stride = Vec3Layouts.VEC3.byteSize();
            for (long offset = 0L; offset < count; offset++) {
                Vec3 va = a.get((int) offset);
                Vec3 vb = b.get((int) offset);

                Vec3Layouts.X.set(segA, stride * offset, va.x());
                Vec3Layouts.Y.set(segA, stride * offset, va.y());
                Vec3Layouts.Z.set(segA, stride * offset, va.z());

                Vec3Layouts.X.set(segB, stride * offset, vb.x());
                Vec3Layouts.Y.set(segB, stride * offset, vb.y());
                Vec3Layouts.Z.set(segB, stride * offset, vb.z());
            }

            Vec3Layouts.VEC3_ADD_ALL.invokeExact(segA, segB, segR, (int) count);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
