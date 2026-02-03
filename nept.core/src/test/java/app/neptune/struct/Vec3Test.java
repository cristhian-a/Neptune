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

            Vec3Layouts.X.set(vec, 0, 3f);
            Vec3Layouts.Y.set(vec, 0, 4f);
            Vec3Layouts.Z.set(vec, 0, 0f);

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

            Vec3Layouts.X.set(vec, 0, 3f);
            Vec3Layouts.Y.set(vec, 0, 4f);
            Vec3Layouts.Z.set(vec, 0, 0f);

            Vec3Layouts.VEC3_NORMALIZE.invokeExact(vec);
            float nx = (float) Vec3Layouts.X.get(vec, 0);
            float ny = (float) Vec3Layouts.Y.get(vec, 0);
            float nz = (float) Vec3Layouts.Z.get(vec, 0);
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
            Vec3Layouts.X.set(vec, 0, 3f);
            Vec3Layouts.Y.set(vec, 0, 4f);
            Vec3Layouts.Z.set(vec, 0, 0f);

            MemorySegment vec2 = arena.allocate(Vec3Layouts.VEC3);
            Vec3Layouts.X.set(vec2, 0, 2f);
            Vec3Layouts.Y.set(vec2, 0, 3f);
            Vec3Layouts.Z.set(vec2, 0, 1f);

            var vecOut = (MemorySegment) Vec3Layouts.VEC3_ADD
                    .invokeExact((SegmentAllocator) arena, vec, vec2);
            float nx = (float) Vec3Layouts.X.get(vecOut, 0);
            float ny = (float) Vec3Layouts.Y.get(vecOut, 0);
            float nz = (float) Vec3Layouts.Z.get(vecOut, 0);
            IO.println(nx + ", " + ny + ", " + nz);

            assert nx == vecR.x();
            assert ny == vecR.y();
            assert nz == vecR.z();

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
