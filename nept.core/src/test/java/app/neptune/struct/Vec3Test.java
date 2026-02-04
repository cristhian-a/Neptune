package app.neptune.struct;

import app.neptune.dto.Vec3;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public final class Vec3Test {

    @BeforeAll
    static void load() {
        System.load("C:\\Users\\User\\IdeaProjects\\neptune\\native\\build\\nept_vec.dll");
    }

    @Test
    void lengthTest() {
        final float x = 3f;
        final float y = 4f;
        final float z = 0f;

        final float expected = (float) Math.sqrt(x * x + y * y + z * z);

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment vec = arena.allocate(Vec3Layouts.VEC3);

            Vec3Layouts.X.set(vec, 0L, 3f);
            Vec3Layouts.Y.set(vec, 0L, 4f);
            Vec3Layouts.Z.set(vec, 0L, 0f);

            float len = (float) Vec3Layouts.VEC3_LENGTH.invokeExact(vec);
            IO.println(len);

            assert len == expected;

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void normalizeTest() {
        final float x = 3f;
        final float y = 4f;
        final float z = 0f;

        final float length = (float) Math.sqrt(x * x + y * y + z * z);
        final float expectedX = x / length;
        final float expectedY = y / length;
        final float expectedZ = z / length;

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment vec = arena.allocate(Vec3Layouts.VEC3);

            Vec3Layouts.X.set(vec, 0L, 3f);
            Vec3Layouts.Y.set(vec, 0L, 4f);
            Vec3Layouts.Z.set(vec, 0L, 0f);

            Vec3Layouts.VEC3_NORMALIZE.invokeExact(vec);
            float nx = (float) Vec3Layouts.X.get(vec, 0L);
            float ny = (float) Vec3Layouts.Y.get(vec, 0L);
            float nz = (float) Vec3Layouts.Z.get(vec, 0L);
            IO.println(nx + ", " + ny + ", " + nz);

            assert nx == expectedX;
            assert ny == expectedY;
            assert nz == expectedZ;

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addTest() {
        Vec3 vecA = new Vec3(3f, 4f, 0f);
        Vec3 vecB = new Vec3(2f, 3f, 1f);
        Vec3 vecR = new Vec3(vecA.x() + vecB.x(), vecA.y() + vecB.y(), vecA.z() + vecB.z());

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment vec = arena.allocate(Vec3Layouts.VEC3);
            Vec3Layouts.X.set(vec, 0L, 3f);
            Vec3Layouts.Y.set(vec, 0L, 4f);
            Vec3Layouts.Z.set(vec, 0L, 0f);

            MemorySegment vec2 = arena.allocate(Vec3Layouts.VEC3);
            Vec3Layouts.X.set(vec2, 0L, 2f);
            Vec3Layouts.Y.set(vec2, 0L, 3f);
            Vec3Layouts.Z.set(vec2, 0L, 1f);

            var vecOut = (MemorySegment) Vec3Layouts.VEC3_ADD
                    .invokeExact((SegmentAllocator) arena, vec, vec2);
            float nx = (float) Vec3Layouts.X.get(vecOut, 0L);
            float ny = (float) Vec3Layouts.Y.get(vecOut, 0L);
            float nz = (float) Vec3Layouts.Z.get(vecOut, 0L);
            IO.println(nx + ", " + ny + ", " + nz);

            assert nx == vecR.x();
            assert ny == vecR.y();
            assert nz == vecR.z();

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addAllTest() {
        try (Arena arena = Arena.ofConfined()) {
            int count = 3;

            MemorySegment segA = arena.allocate(Vec3Layouts.VEC3, count);
            MemorySegment segB = arena.allocate(Vec3Layouts.VEC3, count);
            MemorySegment segR = arena.allocate(Vec3Layouts.VEC3, count);

            long stride = Vec3Layouts.VEC3.byteSize();
            for (long offset = 0L; offset < count; offset++) {
                Vec3Layouts.X.set(segA, offset * stride, 1f);
                Vec3Layouts.Y.set(segA, offset * stride, 2f);
                Vec3Layouts.Z.set(segA, offset * stride, 3f);

                Vec3Layouts.X.set(segB, offset * stride, 10f);
                Vec3Layouts.Y.set(segB, offset * stride, 20f);
                Vec3Layouts.Z.set(segB, offset * stride, 30f);
            }

            Vec3Layouts.VEC3_ADD_ALL.invokeExact(segA, segB, segR, count);

            for (long offset = 0L; offset < count; offset++) {
                float x = (float) Vec3Layouts.X.get(segR, offset * stride);
                float y = (float) Vec3Layouts.Y.get(segR, offset * stride);
                float z = (float) Vec3Layouts.Z.get(segR, offset * stride);

                IO.println("vector " + (offset + 1));
                IO.println(x + ", " + y + ", " + z);

                assert x == 11f;
                assert y == 22f;
                assert z == 33f;
            }

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
