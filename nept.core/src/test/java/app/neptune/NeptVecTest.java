package app.neptune;

import app.neptune.dto.Vec3;
import app.neptune.struct.Vec3Layouts;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.foreign.MemorySegment;
import java.util.ArrayList;
import java.util.List;

final class NeptVecTest {

    @BeforeAll
    static void load() {
        System.load("C:\\Users\\User\\IdeaProjects\\neptune\\native\\build\\nept_vec.dll");
    }

    @Test
    void addInplace() {
        int count = 3;
        List<Vec3> vecA = new ArrayList<>();
        List<Vec3> vecB = new ArrayList<>();

        float x = 1f, y = 2f, z = 3f;
        float x2 = 2f, y2 = 4f, z2 = 6f;

        for (int i = 0; i < count; i++) {
            vecA.add(new Vec3(x, y, z));
            vecB.add(new Vec3(x2, y2, z2));
        }

        try (
                var bufferA = new NativeVec3Array(count);
                var bufferB = new NativeVec3Array(count)
        ) {
            for (int i = 0; i < count; i++) {
                bufferA.set(i, vecA.get(i));
                bufferB.set(i, vecB.get(i));
            }

            Vec3Layouts.VEC3_ADD_INPLACE.invokeExact(
                    bufferA.segment(),
                    bufferB.segment(),
                    count
            );

            MemorySegment result = bufferA.segment();
            long stride = Vec3Layouts.VEC3.byteSize();
            for (int i = 0; i < count; i++) {
                float rx = (float) Vec3Layouts.X.get(result, (long) (stride * i));
                float ry = (float) Vec3Layouts.Y.get(result, (long) (stride * i));
                float rz = (float) Vec3Layouts.Z.get(result, (long) (stride * i));

                IO.println("vector " + (i + 1));
                IO.println(rx + ", " + ry + ", " + rz);

                assert rx == (x + x2);
                assert ry == (y + y2);
                assert rz == (z + z2);
            }

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
