package app.neptune;

import app.neptune.struct.Vec3Layouts;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public final class NeptVec {

    public static void call() {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment vec = arena.allocate(Vec3Layouts.VEC3);
            Vec3Layouts.X.set(vec, 0, 3f);
            Vec3Layouts.Y.set(vec, 0, 4f);
            Vec3Layouts.Z.set(vec, 0, 0f);

            float len = (float) Vec3Layouts.VEC3_LENGTH.invokeExact(vec);
            IO.println("vec3_length");
            IO.println(len);

            Vec3Layouts.VEC3_NORMALIZE.invokeExact(vec);
            IO.println("vec3_normalize");
            IO.println(
                    Vec3Layouts.X.get(vec, 0) + ", " +
                    Vec3Layouts.Y.get(vec, 0) + ", " +
                    Vec3Layouts.Z.get(vec, 0)
            );

            MemorySegment vec2 = arena.allocate(Vec3Layouts.VEC3);
            Vec3Layouts.X.set(vec2, 0, 2f);
            Vec3Layouts.Y.set(vec2, 0, 3f);
            Vec3Layouts.Z.set(vec2, 0, 1f);

            var vecOut = (MemorySegment) Vec3Layouts.VEC3_ADD
                    .invokeExact((SegmentAllocator) arena, vec, vec2);
            IO.println("vec3_add");
            IO.println(
                    Vec3Layouts.X.get(vecOut, 0) + ", " +
                    Vec3Layouts.Y.get(vecOut, 0) + ", " +
                    Vec3Layouts.Z.get(vecOut, 0)
            );
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        IO.println();
    }
}
